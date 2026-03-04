/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.se.raspberrypi.entity;

import deu.se.raspberrypi.entity.base.BaseEntity;
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
@Table(name = "post_comment")
@Getter
@Setter
@NoArgsConstructor
public class PostComment extends BaseEntity{

    /* 게시글 FK */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    /* 작성자 FK */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    /* 댓글 내용 */
    @Column(nullable = false, length = 1000)
    private String content;

    /* ===== 생성자 ===== */
    public PostComment(Post post, Member member, String content) {
        this.post = post;
        this.member = member;
        this.content = content;
    }
}
