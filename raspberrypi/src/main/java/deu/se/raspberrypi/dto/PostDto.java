/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.se.raspberrypi.dto;

/**
 * JSP ↔ Controller 컨트롤러에서 @RequestParam을 따로 안 써도, BoardPostDto로 바로 form 데이터를 받을
 * 수 있음
 *
 * 게시글 저장, 조회용 DTO (Create, Read)
 *
 * @author Haruki
 */
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class PostDto {

    private Long id;
    private String ipAddress;
    private String author;
    private String password;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<MultipartFile> files;             // 저장용
    private List<StoredFileDto> attachments;       // 조회용 (저장된 첨부파일 정보)
    private String maskedIp;
    private String formattedCreatedAt;
}
