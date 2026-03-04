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
 * 포트폴리오 저장 요청 DTO
 *
 * @author Haruki
 */
@Getter
@Setter
public class PortfolioSaveRequestDto {

    private String title;
    private String summary;
    private String content;

    private String techStack;

    private LocalDate projectStart;
    private LocalDate projectEnd;

    private MultipartFile thumbnailFile;   // 저장용
    private List<MultipartFile> files;     // 저장용
    private String idempotencyKey;

}
