/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.se.raspberrypi.entity;

/**
 * 게시글 엔티티
 *
 * 2025.11.03 수정
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

    @Column(nullable = false, updatable = false, insertable = false,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
    )
    private LocalDateTime createdAt;

    @Column(nullable = false, updatable = false, insertable = false,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt; // 수정 시간

    // ★ 편의 메서드 
    // 게시글 작성 시 (양방향 관계 동기화)
    public void addAttachment(Attachment file) {
        attachments.add(file);
        file.setPost(this); // FK(post_id) 설정
    }

    // 게시글 수정&삭제 시 첨부파일 관계 해제
    public void removeAttachment(Attachment file) {
        attachments.remove(file);
        file.setPost(null); // 관계 해제
    }
}
