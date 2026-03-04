/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.se.raspberrypi.entity;

import deu.se.raspberrypi.entity.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
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
public class TempAttachment extends BaseEntity {

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
}
