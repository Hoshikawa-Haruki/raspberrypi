/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.se.raspberrypi.entity;

import deu.se.raspberrypi.entity.base.BaseEntity;
import deu.se.raspberrypi.entity.base.CommentCommon;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author Haruki
 */
@Entity
@Table(name = "portfolio_comment")
@Getter
@Setter
@NoArgsConstructor
public class PortfolioComment extends BaseEntity implements CommentCommon{

    /* 게시글 FK */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "portfolio_id", nullable = false)
    private Portfolio portfolio;

    /* 작성자 FK */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(length = 50, nullable = false)
    private String authorNameSnapshot;

    /* 댓글 내용 */
    @Column(nullable = false, length = 1000)
    private String content;

    /* ===== 생성자 ===== */
    public PortfolioComment(Member member, String content) {
        this.member = member;
        this.content = content;
        this.authorNameSnapshot = member.getNickname();
    }
}
