/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.se.raspberrypi.dto;

import deu.se.raspberrypi.entity.BoardPost;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Haruki
 */
@Getter
@Setter
public class BoardPostListDto {

    private Long id;
    private String author;
    private String title;
    private String ipAddress;
    private LocalDateTime createdAt;

    // ★ 목록 변환용 정적 팩토리
    public static BoardPostListDto fromEntity(BoardPost post) {
        BoardPostListDto dto = new BoardPostListDto();
        dto.setId(post.getId());
        dto.setAuthor(post.getAuthor());
        dto.setTitle(post.getTitle());
        dto.setIpAddress(post.getIpAddress());
        dto.setCreatedAt(post.getCreatedAt());
        return dto;
    }

    // 포맷팅
    public String getMaskedIp() {
        if (ipAddress == null) {
            return "-";
        }
        String[] parts = ipAddress.split("\\.");
        return parts.length >= 2 ? parts[0] + "." + parts[1] : ipAddress;
    }

    public String getFormattedCreatedAt() {
        if (createdAt == null) {
            return "";
        }
        return createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }
}
