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
import deu.se.raspberrypi.entity.Member;
import deu.se.raspberrypi.entity.TempAttachment;
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
import deu.se.raspberrypi.repository.TempAttachmentRepository;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final FileService fileService;
    private final TempAttachmentRepository tempAttachmentRepository;

    // CREATE
    public void save(PostDto dto, Member member, HttpServletRequest request) {

        // 1) IP 추출 (서버 측에서 수행)
        String ip = IpUtils.getClientIp(request);
        dto.setIpAddress(ip);

        // 2) DTO → Entity 변환
        Post post = PostMapper.toPostEntity(dto);

        // 로그인 회원 정보 설정
        post.setAuthorId(member);
        post.setAuthorNameSnapshot(member.getNickname());

        // 본문에서 inline 이미지 UUID 추출
        List<String> inlineUuids = extractImageUuids(dto.getContent());

        // Set으로 만들어 검색 : O(1) 복잡도
        Set<String> inlineUuidSet = new HashSet<>(inlineUuids);

        // uploaderId 기준 temp 이미지 조회
        List<TempAttachment> tempList = tempAttachmentRepository.findByUploaderId(member.getId());

        // 승격 & 삭제 목록 분리
        List<TempAttachment> promoteList = new ArrayList<>();
        List<TempAttachment> deleteList = new ArrayList<>();

        for (TempAttachment temp : tempList) {
            if (inlineUuidSet.contains(temp.getUuid())) {
                promoteList.add(temp); // 승격 목록
            } else {
                deleteList.add(temp); // 삭제 목록
            }
        }

        // 승격 처리
        for (TempAttachment temp : promoteList) {
            Attachment att = new Attachment();
            att.setUuid(temp.getUuid());
            att.setExt(temp.getExt());
            att.setOriginalName(temp.getOriginalName());
            // FK 설정 + 양방향 동기화
            post.addAttachment(att);
            // DB 저장
            // attachmentRepository.save(att); (cascade 설정 때문에 없어도 됨)
            // 실제 파일 temp → upload 이동
            fileService.moveTempToUpload(temp.getUuid(), temp.getExt());
        }

        // 이미지 승격 후, content 경로 교체
        String converted = dto.getContent().replace("/upload_temp/", "/upload/");
        post.setContent(converted);

        // temp 폴더 삭제 처리 (temp 파일 제거)
        for (TempAttachment temp : deleteList) {
            fileService.deleteTempFile(temp.getUuid(), temp.getExt());
        }

        // temp DB 전체 일괄 삭제 → 쿼리 1번으로 최적화
        tempAttachmentRepository.deleteAll(tempList);

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
                attachment.setOriginalName(file.getOriginalFilename());
                post.addAttachment(attachment); // 양방향 동기화 (cascade로 attachment 테이블에 자동 저장됨)
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
        // TODO
        // 로그 시스템 분리 필요
        log.info("[BOARD][CREATE] id={}, title='{}', author='{}', ip={}, attachments={}, createdAt={} ",
                post.getId(),
                post.getTitle(),
                post.getAuthorId(),
                post.getAuthorNameSnapshot(),
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
                    dto.setFormattedCreatedAt(PostFormatter.dateFormat(post.getCreatedAt()));
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
        dto.setFormattedCreatedAt(PostFormatter.dateFormat(post.getCreatedAt()));

        return dto;
    }

    // 3. UPDATE
    public void updateWithFiles(Long id, PostUpdateDto dto, Member member) {

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        // 1) 제목 수정
        post.setTitle(dto.getTitle());

        // 2) 첨부파일 삭제 (DB + 실제 파일)
        if (dto.getDeleteFileIds() != null) {
            dto.getDeleteFileIds().forEach(fileId -> {

                // 삭제 대상 찾기
                Attachment targetFile = post.getAttachments().stream()
                        .filter(a -> a.getId().equals(fileId))
                        .findFirst()
                        .orElse(null);

                if (targetFile != null) {
                    // 2-1) 실제 파일 삭제
                    fileService.deletePhysicalFile(targetFile.getUuid(), targetFile.getExt());
                    // 2-2) 엔티티 관계 해제 (DB orphanRemoval로 삭제됨)
                    post.removeAttachment(targetFile);
                }
            });
        }

        // 3) 새 첨부파일 추가
        if (dto.getNewFiles() != null) {
            for (MultipartFile file : dto.getNewFiles()) {
                if (!file.isEmpty()) {
                    StoredFileDto saved = fileService.handleUpload(file);
                    if (saved != null) {
                        Attachment attachment = new Attachment();
                        attachment.setUuid(saved.getUuid());
                        attachment.setExt(saved.getExt());
                        attachment.setOriginalName(file.getOriginalFilename());
                        post.addAttachment(attachment);
                    }
                }
            }
        }

        // 본문에서 inline 이미지 UUID 추출
        List<String> inlineUuids = extractImageUuids(dto.getContent());

        // Set으로 만들어 검색 : O(1) 복잡도
        Set<String> inlineUuidSet = new HashSet<>(inlineUuids);

        // 기존 이미지중 본문에서 삭제된 이미지 리스트에 추가
        List<Attachment> removeList = new ArrayList<>();
        for (Attachment att : post.getAttachments()) {
            if (!inlineUuidSet.contains(att.getUuid())) {
                removeList.add(att);
            }
        }
        // 이미지 삭제
        for (Attachment att : removeList) {
            fileService.deletePhysicalFile(att.getUuid(), att.getExt());
            post.removeAttachment(att); // 연관 관계 삭제 (DB 삭제)
        }

        // uploaderId 기준 temp 이미지 조회
        List<TempAttachment> tempList = tempAttachmentRepository.findByUploaderId(member.getId());

        // 승격 & 삭제 목록 분리
        List<TempAttachment> promoteList = new ArrayList<>();
        List<TempAttachment> deleteList = new ArrayList<>();

        for (TempAttachment temp : tempList) {
            if (inlineUuidSet.contains(temp.getUuid())) {
                promoteList.add(temp); // 승격 목록
            } else {
                deleteList.add(temp); // 삭제 목록
            }
        }

        // 승격처리
        for (TempAttachment temp : promoteList) {
            Attachment att = new Attachment();
            att.setUuid(temp.getUuid());
            att.setExt(temp.getExt());
            att.setOriginalName(temp.getOriginalName());
            post.addAttachment(att); // FK 설정 (cascade면 save 불필요)

            fileService.moveTempToUpload(temp.getUuid(), temp.getExt());
        }

        // 인라인 이미지 temp 경로 → upload 경로로 변환
        String convertedUrl = dto.getContent().replace("/upload_temp/", "/upload/");
        post.setContent(convertedUrl);

        // temp 폴더 삭제 처리 (temp 파일 제거)
        for (TempAttachment temp : deleteList) {
            fileService.deleteTempFile(temp.getUuid(), temp.getExt());
        }

        // temp DB 전체 일괄 삭제 → 쿼리 1번으로 최적화
        tempAttachmentRepository.deleteAll(tempList);

        postRepository.save(post);
    }

    // 4. DELETE
    public void deletePost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found"));

        // 첨부파일 & 이미지 삭제
        for (Attachment att : post.getAttachments()) {
            fileService.deletePhysicalFile(att.getUuid(), att.getExt());
        }
        postRepository.delete(post);
    }

    // 5. 인라인 이미지 UUID 추출 메서드
    private List<String> extractImageUuids(String html) {

        if (html == null) {
            return List.of();
        }
        // /upload/uuid.png
        // /upload_temp/uuid.jpg
        Pattern p = Pattern.compile("/upload(?:_temp)?/([a-zA-Z0-9\\-]+)\\.(png|jpg|jpeg|gif)");
        Matcher m = p.matcher(html);

        List<String> result = new ArrayList<>();
        while (m.find()) {
            result.add(m.group(1)); // uuid
        }
        return result;
    }
}
