/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.se.raspberrypi.dto;

import java.time.LocalDate;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Haruki
 */
@Getter
@Setter
public class PortfolioDto {

    private Long id;

    private Long authorId;
    private String authorNameSnapshot;

    private String title;
    private String summary;
    private String content;

    private String thumbnailUrl; // 썸네일 url
    private String techStack; // 기술스택

    private LocalDate projectStart;
    private LocalDate projectEnd;

    private List<MultipartFile> files;             // 저장용
    private List<StoredFileDto> attachments;       // 조회용 (저장된 첨부파일 정보)
    
    private String formattedCreatedAt;
    private String idempotencyKey;
}
