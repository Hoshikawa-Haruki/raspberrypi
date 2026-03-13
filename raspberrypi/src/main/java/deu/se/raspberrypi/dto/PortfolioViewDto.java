/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.se.raspberrypi.dto;

import java.time.LocalDate;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * 포트폴리오 조회 응답 DTO
 *
 * @author Haruki
 */
@Getter
@Setter
public class PortfolioViewDto {

    private Long id;

    /* 작성자 정보 */
    private Long authorId;
    private String authorNameSnapshot;

    /* 기본 정보 */
    private String title;
    private String content;
    private String summary;

    /* 포트폴리오 메타 */
    private List<String> techStacks;   // ex) ["Spring", "Redis", "Docker"]
    private LocalDate projectStart;
    private LocalDate projectEnd;

    /* 첨부파일 */
    private List<StoredFileDto> attachments;
    private String thumbnailUrl; // 썸네일

    /* 작성일 */
    private String formattedCreatedAt;
}
