/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.se.raspberrypi.dto;

import deu.se.raspberrypi.entity.Portfolio;
import deu.se.raspberrypi.formatter.Formatter;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Haruki
 */
@Getter
@Setter
public class PortfolioListDto {

    private Long id;
    private String title;
    private String summary;
    private String thumbnailUrl;
    private String authorNameSnapshot;
    private String techStack;
    private String formattedProjectPeriod;
    private String formattedCreatedAt;
    private long commentCount; // 댓글 개수

    public static PortfolioListDto createDto(
            Portfolio p,
            long commentCount,
            String thumbnailUrl
    ) {

        PortfolioListDto dto = new PortfolioListDto();

        dto.id = p.getId();
        dto.title = p.getTitle();
        dto.summary = p.getSummary();
        dto.authorNameSnapshot = p.getAuthorNameSnapshot();
        dto.techStack = p.getTechStack();
        dto.commentCount = commentCount;
        dto.thumbnailUrl = thumbnailUrl;

        dto.formattedCreatedAt
                = Formatter.portfolioListDateFormat(p.getCreatedAt());

        if (p.getProjectStart() != null && p.getProjectEnd() != null) {
            dto.formattedProjectPeriod
                    = p.getProjectStart() + " ~ " + p.getProjectEnd();
        }

        return dto;
    }
}
