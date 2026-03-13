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
 * 포트폴리오 수정요청 DTO
 *
 * @author Haruki
 */
@Getter
@Setter
public class PortfolioUpdateDto {

    private Long id;                    // 수정할 포폴 ID
    private String title;
    private String summary;
    private String content;

    private LocalDate projectStart;
    private LocalDate projectEnd;
    private String techStack;
    // 
    private List<Long> deleteFileIds;   // 기존 첨부 중 삭제할 파일들의 id
    private List<MultipartFile> newFiles; // 새로 업로드할 파일들
    private MultipartFile thumbnailFile;
    private String idempotencyKey;
}
