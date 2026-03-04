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
import deu.se.raspberrypi.dto.StoredFileDto;
import deu.se.raspberrypi.entity.Member;
import deu.se.raspberrypi.entity.Portfolio;
import deu.se.raspberrypi.repository.MemberRepository;
import deu.se.raspberrypi.repository.PortfolioRepository;
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
    private final FileService fileService;

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

        portfolioRepository.save(portfolio);

        // 4) 인라인 이미지 처리
        inlineImageService.handleInlineImages(
                portfolio,
                dto.getContent(),
                member.getId()
        );

        // 5) 일반 첨부파일 처리
        attachmentService.addFileAttachments(portfolio, dto.getFiles());

        // 6) 썸네일 처리 (컬럼 방식)
        if (dto.getThumbnailFile() != null && !dto.getThumbnailFile().isEmpty()) {

            StoredFileDto storedFile = fileService.handleUpload(dto.getThumbnailFile());

            if (storedFile != null) {
                portfolio.setThumbnailUrl("/upload/" + storedFile.getUuid() + "." + storedFile.getExt());
            }
        }
    }

    @Transactional(readOnly = true)
    public Page<PortfolioListDto> getPortfolioList(Pageable pageable) {

        // 1. 포트폴리오 엔티티 조회
        Page<Portfolio> page
                = portfolioRepository.findAllByOrderByCreatedAtDesc(pageable);

        // 2. id 목록 추출
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

        // 4. DTO 매핑
        return page.map(p -> {

            long commentCount
                    = commentCountMap.getOrDefault(p.getId(), 0L);

            return new PortfolioListDto(p, commentCount);
        });
    }

}
