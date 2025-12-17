/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.se.raspberrypi.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Haruki
 */
@Entity
@Table(name = "attachment_temp")
@Getter
@Setter
public class TempAttachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // UUID 파일명 (중복 방지)
    @Column(nullable = false, unique = true)
    private String uuid;

    @Column(nullable = false)
    private String originalName;

    @Column(nullable = false, length = 10)
    private String ext;

    // 어떤 사용자의 임시파일인지 구분 (필수)
    @Column(nullable = false)
    private Long uploaderId;

    // 업로드 시점
    @Column(nullable = false, updatable = false, insertable = false,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;
}
