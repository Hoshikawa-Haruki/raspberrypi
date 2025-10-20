/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.se.raspberrypi.service;

/**
 *
 * @author Haruki
 */
import deu.se.raspberrypi.dto.BoardPostDto;
import deu.se.raspberrypi.dto.BoardPostListDto;
import deu.se.raspberrypi.dto.StoredFileDto;
import deu.se.raspberrypi.entity.BoardPost;
import deu.se.raspberrypi.entity.Attachment;
import deu.se.raspberrypi.repository.BoardPostRepository;
import deu.se.raspberrypi.util.IpUtils;
import jakarta.servlet.http.HttpServletRequest;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import static org.springframework.data.domain.Sort.Direction.DESC;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
@RequiredArgsConstructor
public class BoardPostService {

    private final BoardPostRepository boardPostRepository;
    private final FileService fileService;

    // CREATE
    public void save(BoardPostDto dto, HttpServletRequest request) {

        // IP 추출 (서버 측에서 수행)
        String ip = IpUtils.getClientIp(request);
        dto.setIpAddress(ip);

        // 엔티티화
        BoardPost post = dto.toEntity();

        // 파일 업로드 처리
        if (dto.getFiles() != null && !dto.getFiles().isEmpty()) {
            for (MultipartFile file : dto.getFiles()) {
                // FileService 호출 → 실제 저장된 파일명 받기
                StoredFileDto fileDto = fileService.handleUpload(file);

                if (fileDto == null) {
                    continue;
                }

                // 업로드 결과를 엔티티로 변환
                Attachment attachment = new Attachment();
                attachment.setUuid(fileDto.getUuid());
                attachment.setExt(fileDto.getExt());
                attachment.setOriginalName(file.getOriginalFilename());// 원본 파일명
                post.addAttachment(attachment); // 양방향 동기화 (편의 메서드)
            }
        }

        boardPostRepository.save(post);

        // 첨부파일 체크 메서드
        String attachmentsInfo;
        if (post.getAttachments() != null && !post.getAttachments().isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (Attachment file : post.getAttachments()) {
                sb.append(file.getOriginalName())
                        .append(" (")
                        .append(file.getUuid())
                        .append(".")
                        .append(file.getExt())
                        .append("), ");
            }
            sb.setLength(sb.length() - 2);
            attachmentsInfo = post.getAttachments().size() + "ea [" + sb + "]";
        } else {
            attachmentsInfo = "[none]";
        }

        // 작성시간(createdAt) 포맷 변경 (yyyy-MM-dd HH:mm:ss)
        String createdAt = post.getCreatedAt() != null
                ? post.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                : "N/A";

        // 통합 로그
        log.info("[BOARD][CREATE] id={}, title='{}', author='{}', ip={}, attachments={}, createdAt={} ",
                post.getId(),
                post.getTitle(),
                post.getAuthor(),
                post.getIpAddress(),
                attachmentsInfo,
                createdAt);
    }

    // 2. READ (전체 목록 최신순 정렬)
    public List<BoardPostListDto> findAll() {
        return boardPostRepository.findAll(Sort.by(DESC, "id"))
                .stream()
                .map(BoardPostListDto::fromEntity)
                .toList();
    }

    // 2.1 READ (단일 조회)
    public BoardPostDto findById(Long id) {
        BoardPost post = boardPostRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        // Entity → DTO 변환
        return BoardPostDto.fromEntity(post);
    }

    // 3. UPDATE
    public void update(BoardPostDto dto) {
        BoardPost post = boardPostRepository.findById(dto.getId())
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));
        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        boardPostRepository.save(post);
    }

    // 4. DELETE
    public void delete(Long id) {
        boardPostRepository.deleteById(id);
    }
}
