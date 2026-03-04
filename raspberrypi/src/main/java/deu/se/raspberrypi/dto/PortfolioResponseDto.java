/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.se.raspberrypi.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
public class PortfolioResponseDto {

    private Long id;

    private Long authorId;
    private String authorNameSnapshot;

    private String title;
    private String summary;
    private String content;

    private String thumbnailUrl;
    private String techStack;

    private LocalDate projectStart;
    private LocalDate projectEnd;

    private List<StoredFileDto> attachments;

    private LocalDateTime createdAt;

}
