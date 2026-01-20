/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.se.raspberrypi.service;

/**
 *
 * @author Haruki
 */
import deu.se.raspberrypi.dto.MyPostDto;
import deu.se.raspberrypi.dto.PostDto;
import deu.se.raspberrypi.dto.PostListDto;
import deu.se.raspberrypi.dto.PostUpdateDto;
import deu.se.raspberrypi.dto.StoredFileDto;
import deu.se.raspberrypi.entity.Post;
import deu.se.raspberrypi.entity.Attachment;
import deu.se.raspberrypi.entity.AttachmentType;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

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

        // 인라인 이미지 처리 (temp → attachment)
        handleInlineImages(post, dto.getContent(), member.getId());

        // 첨부파일(FILE) 업로드 처리
        addFileAttachments(post, dto.getFiles());

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
    // LEGACY
    @Deprecated
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

    // 2.2 Read (전체 목록 페이징 조회)
    public Page<PostListDto> findAllPage(Pageable pageable) {
        Page<Post> page = postRepository.findAll(pageable);

        return toPostListDtoPage(page);
    }

    // 2.3 Read (검색)
    public Page<PostListDto> searchPost(
            String searchType,
            String keyword,
            Pageable pageable
    ) {
        if (keyword == null || keyword.isBlank()) {
            // 검색어 없으면 기존 전체 조회
            return findAllPage(pageable);
        }

        Page<Post> page = switch (searchType) {
            case "title" ->
                postRepository.searchByTitle(keyword, pageable);
            case "content" ->
                postRepository.searchByContent(keyword, pageable);
            case "writer" ->
                postRepository.searchByWriter(keyword, pageable);
            default ->
                postRepository.searchByTitleOrContent(keyword, pageable);
        };

        return toPostListDtoPage(page);
    }

    // 2.4 게시글 리스트 Dto 변환 메서드
    private Page<PostListDto> toPostListDtoPage(Page<Post> page) {
        return page.map(post -> {
            PostListDto dto = PostMapper.toPostListDto(post);
            dto.setMaskedIp(PostFormatter.maskIp(post.getIpAddress()));
            dto.setFormattedCreatedAt(
                    PostFormatter.dateFormat(post.getCreatedAt())
            );
            return dto;
        });
    }

    // 2.5 마이페이지 개인 작성글 조회
    public Page<MyPostDto> findMyPosts(Long authorId, int page) {

        Pageable pageable = PageRequest.of(
                page,
                5, // 마이페이지는 5개씩
                Sort.by(Sort.Direction.DESC, "createdAt")
        );

        Page<Post> myPostPage
                = postRepository.findByAuthorId_Id(authorId, pageable);

        return myPostPage.map(post -> {
            MyPostDto dto = new MyPostDto();
            dto.setId(post.getId());
            dto.setTitle(post.getTitle());
            dto.setFormattedCreatedAt(
                    PostFormatter.dateFormat(post.getCreatedAt())
            );
            return dto;
        });
    }

    // 3. UPDATE
    public void updateWithFiles(Long id, PostUpdateDto dto, Member member) {

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        // 1) 제목 수정
        post.setTitle(dto.getTitle());

        // 2) 첨부파일 삭제 (DB + 실제 파일)
        removeAttachmentsById(post, dto.getDeleteFileIds());

        // 3) 신규 첨부파일(FILE) 업로드 처리
        addFileAttachments(post, dto.getNewFiles());

        // 본문 기준 인라인 이미지 셋 생성
        Set<String> inlineUuidSet
                = new HashSet<>(extractImageUuids(dto.getContent()));
        // 삭제된 기존 인라인 이미지 처리 (attachment)
        cleanupInlineImages(post, inlineUuidSet);
        // 신규 인라인 이미지 처리 (temp → attachment)
        handleInlineImages(post, dto.getContent(), member.getId());

        postRepository.save(post);
    }

    // 3.1 게시글 수정 : 첨부파일 추가 메서드
    private void addFileAttachments(Post post, List<MultipartFile> files) {

        if (files == null || files.isEmpty()) {
            return;
        }

        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                continue;
            }
            // FileService 호출 → 실제 저장된 파일명 받기// FileService 호출 → 실제 저장된 파일명 받기
            StoredFileDto fileDto = fileService.handleUpload(file);

            if (fileDto == null) {
                continue;
            }

            // 업로드 결과를 엔티티로 변환
            Attachment attachment = new Attachment();
            attachment.setUuid(fileDto.getUuid());
            attachment.setExt(fileDto.getExt());
            attachment.setOriginalName(file.getOriginalFilename());
            attachment.setType(AttachmentType.FILE); // 첨부파일 enum 타입 설정
            post.addAttachment(attachment); // 양방향 동기화 (cascade로 attachment 테이블에 자동 저장됨)
        }
    }

    // 3.2 게시글 수정 : 첨부파일 삭제 메서드
    private void removeAttachmentsById(Post post, List<Long> deleteIds) {

        if (deleteIds == null || deleteIds.isEmpty()) {
            return;
        }

        Set<Long> deleteIdSet = new HashSet<>(deleteIds);

        post.getAttachments().stream() // 첨부파일에 대해서
                .filter(att -> deleteIdSet.contains(att.getId())) // id가 일치하는 파일에 대해서
                .toList() // list로 생성
                .forEach(att -> { // 삭제 수행
                    fileService.deletePhysicalFile(att.getUuid(), att.getExt());
                    post.removeAttachment(att); // 엔티티 관계 해제
                });
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
        Pattern p = Pattern.compile("/upload(?:_temp)?/([a-zA-Z0-9\\-]+)\\.(png|jpg|jpeg|gif|webp)",
                Pattern.CASE_INSENSITIVE); // 대소문자 대응
        Matcher m = p.matcher(html);

        List<String> result = new ArrayList<>();
        while (m.find()) {
            result.add(m.group(1)); // uuid
        }
        return result;
    }

    // 5.1 인라인 이미지 승격/삭제 메서드
    private void handleInlineImages(Post post, String content, Long uploaderId) {

        // 본문에서 인라인 이미지 UUID 추출
        List<String> inlineUuids = extractImageUuids(content);
        // Set으로 만들어 검색 : O(1) 복잡도
        Set<String> inlineUuidSet = new HashSet<>(inlineUuids);

        // uploaderId 기준 temp 이미지 조회
        List<TempAttachment> tempImageList
                = tempAttachmentRepository.findByUploaderId(uploaderId);

        for (TempAttachment temp : tempImageList) {
            if (inlineUuidSet.contains(temp.getUuid())) { // tempImage가 본문에 있으면
                // 승격 처리
                Attachment att = new Attachment();
                att.setUuid(temp.getUuid());
                att.setExt(temp.getExt());
                att.setOriginalName(temp.getOriginalName());
                // type 기본값 = INLINE
                post.addAttachment(att); // FK 설정 + 양방향 동기화
                // 실제 파일 temp → upload 이동 (승격)
                fileService.moveTempToUpload(temp.getUuid(), temp.getExt());
            } else { // 본문에 없으면
                // 미사용 tempImage 삭제
                fileService.deleteTempFile(temp.getUuid(), temp.getExt());
            }
        }

        // 이미지 승격 후, content 경로 치환
        post.setContent(
                content.replace("/upload_temp/", "/upload/")
        );

        // temp DB 정리 → 쿼리 1번으로 최적화
        tempAttachmentRepository.deleteAll(tempImageList);
    }

    // 5.2 게시글 수정 : 본문(content) 기준 제거된 인라인 이미지(AttachmentType.INLINE) 정리
    private void cleanupInlineImages(Post post, Set<String> inlineUuidSet) {

        post.getAttachments().stream() // List<Attachment>를 stream으로 변환
                .filter(att -> att.getType() == AttachmentType.INLINE) // 조건1 : 인라인만
                .filter(att -> !inlineUuidSet.contains(att.getUuid())) // 조건2 : 본문에 없는 uuid만 남김 (삭제대상)
                .toList() // 결과를 새로운 List로 생성 (ConcurrentModification 방지)
                .forEach(att -> { // 해당 리스트를 대상으로 for each 수행
                    fileService.deletePhysicalFile(att.getUuid(), att.getExt()); // 실제 파일 삭제
                    post.removeAttachment(att);                                  // DB row 삭제(orphanRemoval)
                });
    }
}
