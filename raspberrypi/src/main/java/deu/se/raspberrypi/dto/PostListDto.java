/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.se.raspberrypi.dto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * 전체/ 검색 게시글 리스트 반환용 DTO
 *
 * 2025.11.03 수정
 * @author Haruki
 */
@Getter
@Setter
public class PostListDto {

    private Long id;
    private String authorNameSnapshot;
    private String title;
    private String ipAddress; // ip주소. 페이지에서 직접 보여주지 않으므로 삭제 OK
    private LocalDateTime createdAt; // 원본시간. 페이지에서 직접 보여주지 않으므로 삭제 OK
    private String maskedIp;
    private String formattedCreatedAt;
}
