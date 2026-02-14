/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.se.raspberrypi.controller;

import deu.se.raspberrypi.dto.CommentCreateDto;
import deu.se.raspberrypi.dto.CommentReadDto;
import deu.se.raspberrypi.security.CustomUserDetails;
import deu.se.raspberrypi.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Haruki
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentApiController {

    private final CommentService commentService;

    @GetMapping
    public Page<CommentReadDto> commentList(
            @RequestParam Long postId,
            @PageableDefault(size = 10) Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        Long loginMemberId = (user != null)
                ? user.getMemberId()
                : null;

        return commentService.findCommentByPostId(postId, loginMemberId, pageable);
    }

    /* 다음 형태로 응답 JSON 반환
        {
        "content": [ { 댓글 DTO }, { 댓글 DTO } ],
        "number": 0,
        "totalPages": 4,
        "totalElements": 73,
        "size": 10
        }
     */
    @PostMapping
    public void createComment(
            @RequestBody CommentCreateDto dto,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        commentService.createComment(dto, user.getMemberId());
    }

    @DeleteMapping("/{id}")
    public void deleteComment(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        commentService.deleteComment(id, user.getMemberId());
    }
}
