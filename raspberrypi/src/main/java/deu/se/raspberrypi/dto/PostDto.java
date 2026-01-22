/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.se.raspberrypi.dto;

/**
 * JSP ↔ Controller 컨트롤러에서 @RequestParam을 따로 안 써도, BoardPostDto로 바로 form 데이터를 받을
 * 수 있음
 *
 * 게시글 저장, 단일 게시글 조회용 DTO (Create, Read)
 *
 * 2025.11.03. 수정
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
    private String ipAddress; // ip주소. 페이지에서 직접 보여주지 않으므로 삭제 OK
    private Long authorId;
    private String authorNameSnapshot;
    private String title;
    private String content;
    private LocalDateTime createdAt; // 원본시간. 페이지에서 직접 보여주지 않으므로 삭제 OK
    private LocalDateTime updatedAt; // 수정시간. 페이지에서 직접 보여주지 않으므로 삭제 OK
    private List<MultipartFile> files;             // 저장용
    private List<StoredFileDto> attachments;       // 조회용 (저장된 첨부파일 정보)
    private String maskedIp;
    private String formattedCreatedAt;
}
