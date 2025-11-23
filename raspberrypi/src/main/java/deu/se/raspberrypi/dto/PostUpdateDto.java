/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.se.raspberrypi.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

/**
 * 게시글 수정 시 사용되는 DTO
 * 클라이언트의 name 값이 DTO의 필드명과 같으면 자동 매핑됨.
 *
 * @author Haruki
 */

@Getter
@Setter
public class PostUpdateDto {

    private Long id;                    // 수정할 게시글 ID
    private String title;
    private String content;
    private List<Long> deleteFileIds;   // 기존 첨부 중 삭제할 파일들의 id
    private List<MultipartFile> newFiles; // 새로 업로드할 파일들
}
