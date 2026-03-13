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

    // 2. 리스트 호출
    @Transactional(readOnly = true)
    public Page<PortfolioListDto> getPortfolioList(Pageable pageable) {

        // 1. 포트폴리오 엔티티 조회
        Page<Portfolio> page
                = portfolioRepository.findAllByOrderByCreatedAtDesc(pageable);

        // 2. 게시글 id 목록 추출
        List<Long> ids = page.getContent()
                .stream()
                .map(Portfolio::getId)
                .toList();

        // 3. 댓글수 집계
        Map<Long, Long> commentCountMap = new HashMap<>();

        if (!ids.isEmpty()) {
            List<Object[]> counts
                    = portfolioRepository.countComments(ids);

            for (Object[] row : counts) {
                Long id = (Long) row[0];
                Long count = (Long) row[1];
                commentCountMap.put(id, count);
            }
        }

        // 4. 썸네일 로드
        Map<Long, String> thumbnailMap = new HashMap<>();

        if (!ids.isEmpty()) {
            List<Attachment> thumbnails
                    = attachmentRepository.findPortfolioListThumbnails(ids, AttachmentType.THUMBNAIL);

            for (Attachment att : thumbnails) {
                thumbnailMap.put(att.getPortfolio().getId(), att.getUrl());
            }
        }

        // 5. DTO 매핑
        return page.map(p -> { // p로 특정작업 수행 (FOR문)
            // page 내부 Portfolio들을 하나씩 p로 꺼내서
            // Portfolio → PortfolioListDto로 변환

            long commentCount
                    = commentCountMap.getOrDefault(p.getId(), 0L); // 댓글 갯수 하나씩 꺼내옴?

            String thumbnail
                    = thumbnailMap.getOrDefault(p.getId(), DEFAULT_THUMBNAIL); // 각 포폴에 썸네일 매핑

            return PortfolioListDto.createDto(
                    p,
                    commentCount,
                    thumbnail
            );
        });
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
