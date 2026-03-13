/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.se.raspberrypi.entity.base;

import deu.se.raspberrypi.entity.AttachmentType;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

/**
 * 첨부파일 공통 필드 추상 클래스
 *
 * @author Haruki
 */
@MappedSuperclass
@Getter
@Setter
public abstract class BaseAttachment extends BaseEntity {

    @Column(nullable = false, unique = true)
    protected String uuid;

    @Column(nullable = false)
    protected String originalName;

    @Column(nullable = false, length = 10)
    protected String ext;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    protected AttachmentType type = AttachmentType.INLINE; // 기본: 인라인 이미지)

    public String getUrl() {
        return "/upload/" + uuid + "." + ext;
    }
}
