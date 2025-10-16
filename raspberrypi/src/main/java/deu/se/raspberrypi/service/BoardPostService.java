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
import deu.se.raspberrypi.dto.StoredFileDto;
import deu.se.raspberrypi.entity.BoardPost;
import deu.se.raspberrypi.entity.Attachment;
import deu.se.raspberrypi.repository.BoardPostRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
@RequiredArgsConstructor
public class BoardPostService {

    private final BoardPostRepository boardPostRepository;
    private final FileService fileService;

    // CREATE
    public void save(BoardPostDto dto) {
        BoardPost post = dto.toEntity();

        // 파일 업로드 처리
        if (dto.getFiles() != null && !dto.getFiles().isEmpty()) {
            for (MultipartFile file : dto.getFiles()) {
                // FileService 호출 → 실제 저장된 파일명 받기
                StoredFileDto stored = fileService.handleUpload(file);

                if (stored == null) {
                    continue;
                }

                // 업로드 결과를 엔티티로 변환
                Attachment attachment = new Attachment();
                attachment.setOriginalName(file.getOriginalFilename());// 원본 파일명
                attachment.setSavedName(stored.getSavedName()); // 저장 이름 (UUID)
                attachment.setFilePath(stored.getFullPath()); // 저장 경로
                attachment.setPost(post);

                post.addAttachment(attachment); // 양방향 동기화 (편의 메서드)
            }
        }

        boardPostRepository.save(post);
    }

    // 2. READ (전체 목록)
    public List<BoardPostDto> findAll() {
        return boardPostRepository.findAll().stream()
                .map(post -> {
                    BoardPostDto dto = new BoardPostDto();
                    dto.setId(post.getId());
                    dto.setWriter(post.getWriter());
                    dto.setTitle(post.getTitle());
                    dto.setContent(post.getContent());
                    dto.setCreatedAt(post.getCreatedAt());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    // 2.1 READ (단일 조회)
    public BoardPostDto findById(Long id) {
        return boardPostRepository.findById(id)
                .map(post -> {
                    BoardPostDto dto = new BoardPostDto();
                    dto.setId(post.getId());
                    dto.setWriter(post.getWriter());
                    dto.setTitle(post.getTitle());
                    dto.setContent(post.getContent());
                    dto.setCreatedAt(post.getCreatedAt());
                    return dto;
                })
                .orElse(null);
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
