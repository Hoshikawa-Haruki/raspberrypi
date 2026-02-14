/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.se.raspberrypi.controller;

import deu.se.raspberrypi.dto.MyPostDto;
import deu.se.raspberrypi.security.CustomUserDetails;
import deu.se.raspberrypi.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Haruki
 */
@RestController
@RequestMapping("/api/myposts")
@RequiredArgsConstructor
public class MyPageApiController {

    private static final int MY_PAGE_SIZE = 5;

    private final PostService postService;

    @GetMapping
    public Page<MyPostDto> myPosts(
            @AuthenticationPrincipal CustomUserDetails user,
            @PageableDefault(
                    size = MY_PAGE_SIZE,
                    sort = "createdAt",
                    direction = Sort.Direction.DESC
            ) Pageable pageable
    ) {
        return postService.findMyPosts(
                user.getMemberId(),
                pageable
        );
    }
}
