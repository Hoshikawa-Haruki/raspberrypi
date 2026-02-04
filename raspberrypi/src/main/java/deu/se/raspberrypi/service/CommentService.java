/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.se.raspberrypi.service;

import deu.se.raspberrypi.dto.CommentCreateDto;
import deu.se.raspberrypi.dto.CommentReadDto;
import deu.se.raspberrypi.entity.Comment;
import deu.se.raspberrypi.entity.Member;
import deu.se.raspberrypi.entity.Post;
import deu.se.raspberrypi.formatter.Formatter;
import deu.se.raspberrypi.repository.CommentRepository;
import deu.se.raspberrypi.repository.PostRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Haruki
 */
@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    public void createComment(CommentCreateDto dto, Member loginMember) {
        Post post = postRepository.findById(dto.getPostId())
                .orElseThrow();

        Comment comment = new Comment(post, loginMember, dto.getContent());
        commentRepository.save(comment);
    }

    @Transactional(readOnly = true)
    public List<CommentReadDto> findCommentByPostId(Long postId, Long loginMemberId) {

        return commentRepository.findByPostIdOrderByCreatedAtAsc(postId)
                .stream()
                .map(c -> {
                    CommentReadDto dto = new CommentReadDto();
                    dto.setId(c.getId());
                    dto.setAuthorNameSnapshot(c.getMember().getNickname());
                    dto.setContent(c.getContent());
                    dto.setFormattedCreatedAt(Formatter.commentDateFormat(c.getCreatedAt()));
                    dto.setMine(
                            loginMemberId != null
                            && c.getMember().getId().equals(loginMemberId)
                    );
                    return dto;
                })
                .toList();
    }

}
