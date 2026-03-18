/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.se.raspberrypi.mapper;

import deu.se.raspberrypi.dto.CommentReadDto;
import deu.se.raspberrypi.entity.base.CommentCommon;
import deu.se.raspberrypi.formatter.Formatter;

/**
 * 댓글 조회 시 entity -> Dto 변환 클래스
 * @author Haruki
 */
public class CommentMapper {

    public static CommentReadDto toDto(CommentCommon c, Long loginMemberId) {
        CommentReadDto dto = new CommentReadDto();
        dto.setId(c.getId());
        dto.setAuthorNameSnapshot(c.getAuthorNameSnapshot());
        dto.setContent(c.getContent());
        dto.setFormattedCreatedAt(
                Formatter.commentDateFormat(c.getCreatedAt())
        );
        dto.setMine(
                loginMemberId != null
                && c.getMember().getId().equals(loginMemberId)
        );
        return dto;
    }
}
