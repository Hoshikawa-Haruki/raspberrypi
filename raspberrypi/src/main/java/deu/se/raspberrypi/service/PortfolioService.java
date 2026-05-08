/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.se.raspberrypi.service;

/**
 *
 * @author Haruki
 */
import deu.se.raspberrypi.dto.PortfolioListDto;
import deu.se.raspberrypi.dto.PortfolioSaveRequestDto;
import deu.se.raspberrypi.dto.PortfolioUpdateDto;
import deu.se.raspberrypi.dto.PortfolioViewDto;
import deu.se.raspberrypi.entity.Attachment;
import deu.se.raspberrypi.entity.AttachmentType;
import deu.se.raspberrypi.entity.Member;
import deu.se.raspberrypi.entity.Portfolio;
import deu.se.raspberrypi.formatter.Formatter;
import deu.se.raspberrypi.idempotency.IdempotencyStore;
import deu.se.raspberrypi.mapper.PostMapper;
import deu.se.raspberrypi.repository.AttachmentRepository;
import deu.se.raspberrypi.repository.MemberRepository;
import deu.se.raspberrypi.repository.PortfolioRepository;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;
    private final MemberRepository memberRepository;
    private final AttachmentService attachmentService;
    private final InlineImageService inlineImageService;
    private final IdempotencyStore idempotencyStore;
    private final AttachmentRepository attachmentRepository;

    private static final String DEFAULT_THUMBNAIL = "/images/redhood_404thumb.png";

    // 1. 저장
    public void save(PortfolioSaveRequestDto dto, Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원 정보가 존재하지 않습니다."));

        // 2) DTO → Entity 변환
        Portfolio portfolio = new Portfolio();
        portfolio.setAuthor(member);
        portfolio.setAuthorNameSnapshot(member.getNickname());
        portfolio.setTitle(dto.getTitle());
        portfolio.setSummary(dto.getSummary());
        portfolio.setContent(dto.getContent());
        portfolio.setTechStack(dto.getTechStack());
        portfolio.setProjectStart(dto.getProjectStart());
        portfolio.setProjectEnd(dto.getProjectEnd());

        // 4) 인라인 이미지 처리
        inlineImageService.handleInlineImages(
                portfolio,
                dto.getContent(),
                member.getId()
        );

        // 5) 일반 첨부파일 처리
        attachmentService.addFileAttachments(portfolio, dto.getFiles());

        // 6) 썸네일 처리
        attachmentService.addThumbnail(portfolio, dto.getThumbnailFile());

        // 7) 저장
        portfolioRepository.save(portfolio);
    }

    // 2. 리스트 호출 (검색 없음)
    @Transactional(readOnly = true)
    public Page<PortfolioListDto> getPortfolioList(Pageable pageable) {
        Page<Portfolio> page = portfolioRepository.findAllByOrderByCreatedAtDesc(pageable);
        return toListDto(page);
    }

    // 2-1. 리스트 호출 (검색 포함)
    @Transactional(readOnly = true)
    public Page<PortfolioListDto> getPortfolioList(String searchType, String keyword, Pageable pageable) {

        if (keyword == null || keyword.isBlank()) {
            return getPortfolioList(pageable);
        }

        Page<Portfolio> page = switch (searchType) {
            case "title"   -> portfolioRepository.findByTitleContaining(keyword, pageable);
            case "content" -> portfolioRepository.findByContentContaining(keyword, pageable);
            case "writer"  -> portfolioRepository.findByAuthorNameSnapshotContaining(keyword, pageable);
            default        -> portfolioRepository.findByTitleOrContentOrSummaryContaining(keyword, pageable);
        };

        return toListDto(page);
    }

        // 1. 포트폴리오 엔티티 조회
    // 공통 매핑 로직 (댓글수 + 썸네일 → DTO)
    private Page<PortfolioListDto> toListDto(Page<Portfolio> page) {

        // 2. 게시글 id 목록 추출
        List<Long> ids = page.getContent()
                .stream()
                .map(Portfolio::getId)
                .toList();

        // 3. 댓글수 집계
        Map<Long, Long> commentCountMap = new HashMap<>();
        if (!ids.isEmpty()) {
            for (Object[] row : portfolioRepository.countComments(ids)) {
                commentCountMap.put((Long) row[0], (Long) row[1]);
            }
        }

        // 4. 썸네일 로드
        Map<Long, String> thumbnailMap = new HashMap<>();
        if (!ids.isEmpty()) {
            for (Attachment att : attachmentRepository.findPortfolioListThumbnails(ids, AttachmentType.THUMBNAIL)) {
                thumbnailMap.put(att.getPortfolio().getId(), att.getUrl());
            }
        }

        // 5. DTO 매핑
        return page.map(p -> PortfolioListDto.createDto(
                p,
                commentCountMap.getOrDefault(p.getId(), 0L),
                thumbnailMap.getOrDefault(p.getId(), DEFAULT_THUMBNAIL)
        ));
    }

    // 3. 조회
    @Transactional(readOnly = true)
    public PortfolioViewDto getPortfolioById(Long id) {

        Portfolio portfolio = portfolioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("포트폴리오가 존재하지 않습니다."));
        // DTO 생성
        PortfolioViewDto dto = new PortfolioViewDto();

        dto.setId(portfolio.getId());
        dto.setAuthorId(portfolio.getAuthor().getId());
        dto.setAuthorNameSnapshot(portfolio.getAuthorNameSnapshot());
        dto.setTitle(portfolio.getTitle());
        dto.setContent(portfolio.getContent());
        dto.setSummary(portfolio.getSummary());

        Attachment thumbnail = portfolio.getAttachments().stream()
                .filter(a -> a.getType() == AttachmentType.THUMBNAIL)
                .findFirst()
                .orElse(null);

        if (thumbnail != null) {
            dto.setThumbnailUrl(thumbnail.getUrl());
        }

        if (portfolio.getTechStack() != null && !portfolio.getTechStack().isBlank()) {
            dto.setTechStacks(
                    Arrays.stream(portfolio.getTechStack().split(","))
                            .map(String::trim)
                            .toList()
            );
        }

        dto.setProjectStart(portfolio.getProjectStart());
        dto.setProjectEnd(portfolio.getProjectEnd());
        dto.setFormattedCreatedAt(Formatter.postDateFormat(portfolio.getCreatedAt()));

        if (portfolio.getAttachments() != null && !portfolio.getAttachments().isEmpty()) {
            dto.setAttachments(
                    portfolio.getAttachments().stream()
                            .filter(att -> att.getType() != AttachmentType.THUMBNAIL) // 썸네일은 제외
                            .map(PostMapper::toStoredFileDto)
                            .toList()
            );
        }

        return dto;
    }

    // 4. UPDATE
    public void update(Long id, PortfolioUpdateDto dto, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        // 0) idem 키 생성
        String idemKey = "portfolio:update:" + id + ":" + dto.getIdempotencyKey();
        if (!idempotencyStore.tryAcquire(idemKey)) {
            return; // 중복 요청 → 종료
        }

        Portfolio portfolio = portfolioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        if (!portfolio.getAuthor().getId().equals(memberId)) {
            throw new IllegalStateException("수정 권한 없음");
        }

        // 1) 내용 수정
        portfolio.setTitle(dto.getTitle());
        portfolio.setSummary(dto.getSummary());
        portfolio.setTechStack(dto.getTechStack());
        portfolio.setProjectStart(dto.getProjectStart());
        portfolio.setProjectEnd(dto.getProjectEnd());

        // 2) 첨부파일 삭제 (DB + 실제 파일)
        attachmentService.removeAttachmentsById(portfolio, dto.getDeleteFileIds());

        // 3) 신규 첨부파일(FILE) 업로드 처리
        attachmentService.addFileAttachments(portfolio, dto.getNewFiles());

        // 삭제된 기존 인라인 이미지 처리 (attachment)
        inlineImageService.cleanupInlineImages(portfolio, dto.getContent());
        // 신규 인라인 이미지 처리 (temp → attachment)
        inlineImageService.handleInlineImages(portfolio, dto.getContent(), member.getId());

        // 썸네일 수정
        attachmentService.updateThumbnail(portfolio, dto.getThumbnailFile());

        portfolioRepository.save(portfolio);
    }

    // 5. DELETE
    public void delete(Long id) {
        Portfolio portfolio = portfolioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found"));

        // 첨부파일 삭제
        attachmentService.deleteAllAttachments(portfolio);
        portfolioRepository.delete(portfolio);
    }
}
