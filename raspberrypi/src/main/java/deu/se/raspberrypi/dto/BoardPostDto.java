/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.se.raspberrypi.dto;

/**
 * 게시글 입력/조회용 DTO JSP ↔ Controller 컨트롤러에서 @RequestParam을 따로 안 써도, BoardPostDto로
 * 바로 form 데이터를 받을 수 있음
 *
 * @author Haruki
 */
import deu.se.raspberrypi.entity.BoardPost;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class BoardPostDto {

    private Long id;
    private String ipAddress;
    private String author;
    private String password;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<MultipartFile> files;             // 업로드용
    private List<StoredFileDto> attachments;       // 조회용 (저장된 첨부파일 정보)

    // 1. 게시글 작성
    public BoardPost toEntity() { // DTO → Entity 변환, BoardPostDto → BoardPost로 변환해주는 팩토리 메서드
        BoardPost post = new BoardPost();
        post.setId(id);
        post.setIpAddress(ipAddress);
        post.setAuthor(author);
        post.setPassword(password);
        post.setTitle(title);
        post.setContent(content);
        return post;
    }

    // 2. 게시글 단일 조회
    public static BoardPostDto fromEntity(BoardPost post) {
        BoardPostDto dto = new BoardPostDto();
        dto.setId(post.getId());
        dto.setIpAddress(post.getIpAddress());
        dto.setAuthor(post.getAuthor());
        dto.setTitle(post.getTitle());
        dto.setContent(post.getContent());
        dto.setCreatedAt(post.getCreatedAt());
        dto.setUpdatedAt(post.getUpdatedAt());

        // 첨부파일이 있으면 StoredFileDto로 매핑
        if (post.getAttachments() != null && !post.getAttachments().isEmpty()) {
            dto.setAttachments(
                    post.getAttachments().stream()
                            .map(att -> {
                                StoredFileDto fileDto = new StoredFileDto();
                                fileDto.setSavedName(att.getSavedName());
                                fileDto.setFullPath(att.getFilePath());
                                return fileDto;
                            })
                            .toList()
            );
        }

        return dto;
    }

    // 3. 포맷팅된 날짜 반환 메서드
    public String getFormattedCreatedAt() {
        if (createdAt == null) {
            return "";
        }
        return createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    /**
     * 사용자의 IP 주소 중 앞부분만 남기고 나머지는 제거 예: 27.35.110.2 → 27.35
     *
     * @return
     */
    // 4. IP주소 마스킹 메서드
    public String getMaskedIp() {
        if (ipAddress == null) {
            return "-";
        }

        // IPv4 형태인 경우
        if (ipAddress.contains(".")) {
            String[] parts = ipAddress.split("\\.");
            if (parts.length >= 2) {
                return parts[0] + "." + parts[1]; // 앞 2세그먼트만 반환
            } else {
                return ipAddress;
            }
        }

        // IPv6 형태인 경우 (간단히 앞 2부분만 표시)
        if (ipAddress.contains(":")) {
            String[] parts = ipAddress.split(":");
            if (parts.length >= 2) {
                return parts[0] + ":" + parts[1];
            } else {
                return ipAddress;
            }
        }

        return ipAddress;
    }
}
