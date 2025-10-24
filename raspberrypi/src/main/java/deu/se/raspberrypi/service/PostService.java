/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.se.raspberrypi.service;

/**
 *
 * @author Haruki
 */
import deu.se.raspberrypi.dto.PostDto;
import deu.se.raspberrypi.dto.PostListDto;
import deu.se.raspberrypi.dto.PostUpdateDto;
import deu.se.raspberrypi.dto.StoredFileDto;
import deu.se.raspberrypi.entity.Post;
import deu.se.raspberrypi.entity.Attachment;
import deu.se.raspberrypi.formatter.PostFormatter;
import deu.se.raspberrypi.mapper.PostMapper;
import deu.se.raspberrypi.util.IpUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import static org.springframework.data.domain.Sort.Direction.DESC;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import deu.se.raspberrypi.repository.PostRepository;
import java.time.LocalDateTime;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final FileService fileService;

    // CREATE
    public void save(PostDto dto, HttpServletRequest request) {

        // 1) IP 추출 (서버 측에서 수행)
        String ip = IpUtils.getClientIp(request);
        dto.setIpAddress(ip);

        // 2) DTO → Entity 변환 (Mapper 사용)
        Post post = PostMapper.toPostEntity(dto);

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

        postRepository.save(post);

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
    public List<PostListDto> findAll() {
        return postRepository.findAll(Sort.by(DESC, "id"))
                .stream()
                .map(post -> {
                    PostListDto dto = PostMapper.toPostListDto(post);
                    // 목록에서도 표시용 데이터 가공
                    dto.setMaskedIp(PostFormatter.maskIp(post.getIpAddress()));
                    dto.setFormattedCreatedAt(PostFormatter.dateformat(post.getCreatedAt()));
                    return dto;
                })
                .toList();
    }

    // 2.1 READ (단일 조회)
    public PostDto findById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
        PostDto dto = PostMapper.toPostDto(post);

        // DTO에 표시용 값 주입
        dto.setMaskedIp(PostFormatter.maskIp(post.getIpAddress()));
        dto.setFormattedCreatedAt(PostFormatter.dateformat(post.getCreatedAt()));

        return dto;
    }

    // 3. UPDATE
    public void updateWithFiles(Long id, PostUpdateDto dto) {

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        // 1) 제목/내용 수정
        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());

        // 2) 첨부파일 삭제 (DB + orphanRemoval로 실제 삭제됨)
        if (dto.getDeleteFileIds() != null) {
            dto.getDeleteFileIds().forEach(post::removeAttachmentById);
        }

        // 3) 새 파일 업로드 추가
        if (dto.getNewFiles() != null) {
            for (MultipartFile file : dto.getNewFiles()) {
                if (!file.isEmpty()) {
                    StoredFileDto saved = fileService.handleUpload(file);
                    if (saved != null) {
                        Attachment attach = new Attachment();
                        attach.setUuid(saved.getUuid());
                        attach.setExt(saved.getExt());
                        attach.setOriginalName(file.getOriginalFilename());
                        post.addAttachment(attach);
                    }
                }
            }
        }

        // 4) 수정시간 갱신
        post.setUpdatedAt(LocalDateTime.now());
    }

    // 3. UPDATE    
    public void update(PostDto dto) {
        Post post = postRepository.findById(dto.getId())
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));
        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        postRepository.save(post);
    }

    // 4. DELETE
    public void delete(Long id) {
        postRepository.deleteById(id);
    }
}
