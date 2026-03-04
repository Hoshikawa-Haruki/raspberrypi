/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.se.raspberrypi.entity;

/**
 * 게시글 엔티티
 *
 * 2025.11.03 수정
 *
 * @author Haruki
 */
import deu.se.raspberrypi.entity.base.BaseEntity;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "board_post")
@Getter
@Setter
public class Post extends BaseEntity implements ContentEntity {

    // 회원 FK
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author_id")
    private Member authorId;

    // 작성자명 스냅샷 (닉네임 저장용)
    @Column(length = 50, nullable = false)
    private String authorNameSnapshot;

    // ip주소
    @Column(nullable = false, length = 50)
    private String ipAddress;

    @Column(nullable = false, length = 200)
    private String title; // 게시글 제목

    @Column(columnDefinition = "LONGTEXT", nullable = false)
    private String content; // 게시글 내용

    // 게시글 1개 → 여러 파일 (1:N)
    @OneToMany(mappedBy = "post",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private List<Attachment> attachments = new ArrayList<>();

    // 댓글
    @OneToMany(
            mappedBy = "post",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<PostComment> comments = new ArrayList<>();

    @Override
    public void addAttachment(Attachment file) {
        attachments.add(file); // add (post -> attachment 리스트)
        file.setPost(this);  // set FK(post_id) 설정
        file.setPortfolio(null); // 반대쪽 fk null 설정
    }

    @Override
    public void removeAttachment(Attachment file) {
        attachments.remove(file);
        file.setPost(null);
    }

    public void addComment(PostComment comment) {
        comments.add(comment);
        comment.setPost(this);
    }

    public void removeComment(PostComment comment) {
        comments.remove(comment);
        comment.setPost(null);
    }

}
