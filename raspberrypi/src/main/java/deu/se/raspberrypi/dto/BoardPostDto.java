/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.se.raspberrypi.dto;

/**
 * 게시글 입력/조회용 DTO
 * JSP ↔ Controller
 * 
 * 컨트롤러에서 @RequestParam을 따로 안 써도, BoardPostDto로 바로 form 데이터를 받을 수 있음
 * @author Haruki
 */

import deu.se.raspberrypi.entity.BoardPost;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;


@Getter
@Setter
public class BoardPostDto {

    private Long id;
    private String writer;
    private String password;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<MultipartFile> files; // DB용이 아니라 업로드 요청을 받기 위한 임시 필드
 
    public BoardPost toEntity() { // DTO → Entity 변환, BoardPostDto → BoardPost로 변환해주는 팩토리 메서드
        BoardPost post = new BoardPost();
        post.setWriter(writer);
        post.setPassword(password);
        post.setTitle(title);
        post.setContent(content);
        return post;
    }
    
    public static BoardPostDto fromEntity(BoardPost post) { // 조회용
        BoardPostDto dto = new BoardPostDto();
        dto.setId(post.getId());
        dto.setWriter(post.getWriter());
        dto.setTitle(post.getTitle());
        dto.setContent(post.getContent());
        dto.setCreatedAt(post.getCreatedAt());
        dto.setUpdatedAt(post.getUpdatedAt());
        return dto;
    }

}
