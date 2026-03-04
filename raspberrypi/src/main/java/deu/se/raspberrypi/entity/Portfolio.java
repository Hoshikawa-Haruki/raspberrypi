/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.se.raspberrypi.entity;

import deu.se.raspberrypi.entity.base.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Haruki
 */
@Entity
@Table(name = "portfolio")
@Getter
@Setter
public class Portfolio extends BaseEntity implements ContentEntity {

    // 작성자 FK
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author_id")
    private Member author;

    @Column(nullable = false)
    private String authorNameSnapshot;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, length = 200)
    private String summary; // 카드용 요약

    @Column(columnDefinition = "LONGTEXT", nullable = false)
    private String content;

    // 첨부파일
    @OneToMany(mappedBy = "portfolio",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private List<Attachment> attachments = new ArrayList<>();

    // 댓글
    @OneToMany(
            mappedBy = "portfolio",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<PortfolioComment> comments = new ArrayList<>();

    @Column(length = 500)
    private String thumbnailUrl;

    @Column(length = 500)
    private String techStack;

    private LocalDate projectStart;

    private LocalDate projectEnd;

    @Override
    public void addAttachment(Attachment file) {
        attachments.add(file);
        file.setPortfolio(this);   // set FK
        file.setPost(null);        // 반대쪽 fk 제거 (선택)
    }

    @Override
    public void removeAttachment(Attachment file) {
        attachments.remove(file); // 목록 삭제
        file.setPortfolio(null); // 관계 해제
    }

    public void addComment(PortfolioComment comment) {
        comments.add(comment);
        comment.setPortfolio(this);
    }

    public void removeComment(PortfolioComment comment) {
        comments.remove(comment);
        comment.setPortfolio(null);
    }
}
