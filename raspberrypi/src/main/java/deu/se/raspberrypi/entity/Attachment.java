/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.se.raspberrypi.entity;

/**
 * 첨부파일 엔티티
 *
 * @author Haruki
 */
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "attachment_file")
@Getter
@Setter
public class Attachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 파일 고유번호 1 

    @Column(nullable = false, unique = true)
    private String uuid; // UUID 2

    @Column(nullable = false)
    private String originalName; // 원본 파일명 3 (메타데이터로 분리 예정)

    @Column(nullable = false, length = 10)
    private String ext; // 확장자 4

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "post_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(
                    name = "fk_attachment_boardpost",
                    foreignKeyDefinition = "FOREIGN KEY (post_id) REFERENCES board_post(id) ON DELETE CASCADE"
            )
    )
    private BoardPost post; // 게시글 FK 5

    @Column(nullable = false, updatable = false, insertable = false,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt; // 첨부파일 업로드 시점 6
}
