/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.se.raspberrypi.service;

import deu.se.raspberrypi.dto.CommentCreateDto;
import deu.se.raspberrypi.dto.CommentReadDto;
import deu.se.raspberrypi.entity.PostComment;
import deu.se.raspberrypi.entity.Member;
import deu.se.raspberrypi.entity.Portfolio;
import deu.se.raspberrypi.entity.PortfolioComment;
import deu.se.raspberrypi.entity.Post;
import deu.se.raspberrypi.formatter.Formatter;
import deu.se.raspberrypi.repository.MemberRepository;
import deu.se.raspberrypi.repository.PortfolioCommentRepository;
import deu.se.raspberrypi.repository.PortfolioRepository;
import deu.se.raspberrypi.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import deu.se.raspberrypi.repository.PostCommentRepository;

/**
 *
 * @author Haruki
 */
@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final PostRepository postRepository;
    private final PostCommentRepository postCommentRepository;
    private final PortfolioRepository portfolioRepository;
    private final PortfolioCommentRepository portfolioCommentRepository;
    private final MemberRepository memberRepository;

    public void createPostComment(CommentCreateDto dto, Long memberId) {
        Post post = postRepository.findById(dto.getPostId())
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        PostComment comment = new PostComment(post, member, dto.getContent());
        postCommentRepository.save(comment);
    }

    @Transactional(readOnly = true)
    public Page<CommentReadDto> findPostCommentByPostId(
            Long postId,
            Long loginMemberId,
            Pageable pageable) {
        return postCommentRepository.findByPostIdOrderByCreatedAtAsc(postId, pageable)
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
                });
    }

    public void deletePostComment(Long commentId, Long loginMemberId) {
        PostComment comment = postCommentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글 없음"));

        if (!comment.getMember().getId().equals(loginMemberId)) {
            throw new IllegalStateException("삭제 권한 없음");
        }

        postCommentRepository.delete(comment);
    }

    /* === 포트폴리오 Comment 메서드 === */
    
    public void createPortfolioComment(CommentCreateDto dto, Long memberId) {
        Portfolio portfolio = portfolioRepository.findById(dto.getPostId())
                .orElseThrow(() -> new IllegalArgumentException("포트폴리오가 존재하지 않습니다."));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        PortfolioComment comment = new PortfolioComment(portfolio, member, dto.getContent());
        portfolioCommentRepository.save(comment);
    }

    @Transactional(readOnly = true)
    public Page<CommentReadDto> findPortfolioCommentByPortfolioId(
            Long portfolioId,
            Long loginMemberId,
            Pageable pageable) {

        return portfolioCommentRepository
                .findByPortfolioIdOrderByCreatedAtAsc(portfolioId, pageable)
                .map(c -> {
                    CommentReadDto dto = new CommentReadDto();
                    dto.setId(c.getId());
                    dto.setAuthorNameSnapshot(c.getMember().getNickname());
                    dto.setContent(c.getContent());
                    dto.setFormattedCreatedAt(
                            Formatter.commentDateFormat(c.getCreatedAt())
                    );
                    dto.setMine(
                            loginMemberId != null
                            && c.getMember().getId().equals(loginMemberId)
                    );
                    return dto;
                });
    }

    public void deletePortfolioComment(Long commentId, Long loginMemberId) {
        PortfolioComment comment = portfolioCommentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글 없음"));

        if (!comment.getMember().getId().equals(loginMemberId)) {
            throw new IllegalStateException("삭제 권한 없음");
        }

        portfolioCommentRepository.delete(comment);
    }

}
