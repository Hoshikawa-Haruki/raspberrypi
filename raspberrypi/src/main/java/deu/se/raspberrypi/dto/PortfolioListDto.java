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

    // JPQL projection 생성자
    public PortfolioListDto(Portfolio p, long commentCount) {

        this.id = p.getId();
        this.title = p.getTitle();
        this.summary = p.getSummary();
        this.thumbnailUrl = p.getThumbnailUrl();
        this.authorNameSnapshot = p.getAuthorNameSnapshot();
        this.techStack = p.getTechStack();
        this.commentCount = commentCount;

        this.formattedCreatedAt
                = Formatter.portfolioListDateFormat(p.getCreatedAt());

        if (p.getProjectStart() != null && p.getProjectEnd() != null) {
            this.formattedProjectPeriod
                    = p.getProjectStart()
                    + " ~ "
                    + p.getProjectEnd();
        }
    }
}
