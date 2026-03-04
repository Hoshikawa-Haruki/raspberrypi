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
import deu.se.raspberrypi.entity.Post;
import deu.se.raspberrypi.entity.Attachment;
import deu.se.raspberrypi.entity.Member;
import deu.se.raspberrypi.formatter.Formatter;
import deu.se.raspberrypi.idempotency.IdempotencyStore;
import deu.se.raspberrypi.mapper.PostMapper;
import deu.se.raspberrypi.repository.MemberRepository;
import deu.se.raspberrypi.util.IpUtils;
import jakarta.servlet.http.HttpServletRequest;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import static org.springframework.data.domain.Sort.Direction.DESC;
import org.springframework.stereotype.Service;
import deu.se.raspberrypi.repository.PostRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final InlineImageService inlineImageService;
    private final AttachmentService attachmentService;
    private final MemberRepository memberRepository;
    private final IdempotencyStore idempotencyStore;

    // CREATE
    public void save(PostDto dto, Long memberId, HttpServletRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        // 0) idem 키 생성
        String idemKey = "post:create:" + dto.getIdempotencyKey();
        if (!idempotencyStore.tryAcquire(idemKey)) {
            return; // 중복 요청 → 종료
        }

        // 1) IP 추출 (서버 측에서 수행)
        String ip = IpUtils.getClientIp(request);
        dto.setIpAddress(ip);

        // 2) DTO → Entity 변환
        Post post = PostMapper.toPostEntity(dto);

        // 로그인 회원 정보 설정
        post.setAuthorId(member);
        post.setAuthorNameSnapshot(member.getNickname());

        // 인라인 이미지 처리 (temp → attachment)
        inlineImageService.handleInlineImages(post, dto.getContent(), member.getId());

        // 첨부파일(FILE) 업로드 처리
        attachmentService.addFileAttachments(post, dto.getFiles());

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
                    dto.setMaskedIp(Formatter.maskIp(post.getIpAddress()));
                    // dto.setFormattedCreatedAt(Formatter.postDateFormat(post.getCreatedAt()));
                    return dto;
                })
                .toList();
    }

    // 2.1 READ (게시글 열람)
    @Transactional(readOnly = true)
    public PostDto findById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
        PostDto dto = PostMapper.toPostDto(post);

        // DTO에 표시용 값 주입
        dto.setMaskedIp(Formatter.maskIp(post.getIpAddress()));
        dto.setFormattedCreatedAt(Formatter.postDateFormat(post.getCreatedAt()));

        return dto;
    }

    // 2.2 Read (전체 목록 페이징 조회)
    @Transactional(readOnly = true)
    public Page<PostListDto> findAllPage(Pageable pageable) {
        Page<PostListDto> pageList
                = postRepository.findPostList(pageable);
        return applyDisplayNo(pageList);
    }

    // 2.3 Read (검색)
    @Transactional(readOnly = true)
    public Page<PostListDto> searchPost(
            String searchType,
            String keyword,
            Pageable pageable
    ) {
        if (keyword == null || keyword.isBlank()) {
            // 검색어 없으면 기존 전체 조회
            return findAllPage(pageable);
        }

        Page<PostListDto> page = switch (searchType) {
            case "title" ->
                postRepository.searchByTitle(keyword, pageable);
            case "content" ->
                postRepository.searchByContent(keyword, pageable);
            case "writer" ->
                postRepository.searchByWriter(keyword, pageable);
            default ->
                postRepository.searchByTitleOrContent(keyword, pageable);
        };

        return applyDisplayNo(page);
    }

    // 2.4 게시글 리스트 DTO 포맷팅
    @Deprecated
    private Page<PostListDto> toPostListDtoPageForView(Page<Post> page) {
        return page.map(post -> {
            PostListDto dto = PostMapper.toPostListDto(post);
            return dto;
        });
    }

    // 2.5 게시글 표시 번호 적용
    private Page<PostListDto> applyDisplayNo(Page<PostListDto> dtoPage) {

        long startNo = dtoPage.getTotalElements()
                - (long) dtoPage.getNumber() * dtoPage.getSize();

        List<PostListDto> content = dtoPage.getContent();
        for (int i = 0; i < content.size(); i++) {
            content.get(i).setDisplayNo(startNo - i);
        }

        return dtoPage;
    }

    // 2.5 마이페이지 개인 작성글 조회
    @Transactional(readOnly = true)
    public Page<MyPostDto> findMyPosts(Long authorId, Pageable pageable) {
        return postRepository.findMyPostsDto(authorId, pageable);
    }

    // 3. UPDATE
    public void updateWithFiles(Long id, PostUpdateDto dto, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        // 0) idem 키 생성
        String idemKey = "post:update:" + id + ":" + dto.getIdempotencyKey();
        if (!idempotencyStore.tryAcquire(idemKey)) {
            return; // 중복 요청 → 종료
        }

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        if (!post.getAuthorId().getId().equals(memberId)) {
            throw new IllegalStateException("수정 권한 없음");
        }

        // 1) 제목 수정
        post.setTitle(dto.getTitle());

        // 2) 첨부파일 삭제 (DB + 실제 파일)
        attachmentService.removeAttachmentsById(post, dto.getDeleteFileIds());

        // 3) 신규 첨부파일(FILE) 업로드 처리
        attachmentService.addFileAttachments(post, dto.getNewFiles());

        // 삭제된 기존 인라인 이미지 처리 (attachment)
        inlineImageService.cleanupInlineImages(post, dto.getContent());
        // 신규 인라인 이미지 처리 (temp → attachment)
        inlineImageService.handleInlineImages(post, dto.getContent(), member.getId());

        postRepository.save(post);
    }

    // 4. DELETE
    public void deletePost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found"));

        // 첨부파일 삭제
        attachmentService.deleteAllAttachments(post);
        postRepository.delete(post);
    }
}
