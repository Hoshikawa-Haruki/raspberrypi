/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.se.raspberrypi.controller;

import deu.se.raspberrypi.dto.CommentCreateDto;
import deu.se.raspberrypi.security.CustomUserDetails;
import deu.se.raspberrypi.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

/**
 *
 * @author Haruki
 */
@Controller
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/comment/create")
    public String createComment(
            CommentCreateDto dto,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        commentService.createComment(dto, user.getMember());
        return "redirect:/board/view/" + dto.getPostId();
    }
}
