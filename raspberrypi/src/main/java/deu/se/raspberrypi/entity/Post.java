/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.se.raspberrypi.entity;

/**
 * 게시글 엔티티
 *
 * @author Haruki
 */
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "board_post")
@Getter
@Setter
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 게시글 고유번호

    @Column(nullable = false, length = 50)
    private String ipAddress; // ip주소

    @Column(nullable = false, length = 50)
    private String author; // 작성자

    @Column(nullable = false, length = 255)
    private String password; // 글 수정/삭제용 비밀번호

    @Column(nullable = false, length = 200)
    private String title; // 게시글 제목

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content; // 게시글 내용

    // 게시글 1개 → 여러 파일 (1:N)
    @OneToMany(mappedBy = "post",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private List<Attachment> attachments = new ArrayList<>();

    @Column(nullable = false, updatable = false, insertable = false,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
    )
    private LocalDateTime createdAt;

    @Column(nullable = false, insertable = false,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt; // 수정 시간

    // ★ 편의 메서드 (양방향 관계 동기화)
    // 게시글 작성 시
    public void addAttachment(Attachment file) {
        attachments.add(file);
        file.setPost(this); // FK(post_id) 설정
    }

    public void removeAttachment(Attachment file) {
        attachments.remove(file);
        file.setPost(null); // 관계 해제
    }
    
    // 게시글 수정 시
    public void removeAttachmentById(Long attachmentId) {
        attachments.removeIf(att -> att.getId().equals(attachmentId));
    }

}
