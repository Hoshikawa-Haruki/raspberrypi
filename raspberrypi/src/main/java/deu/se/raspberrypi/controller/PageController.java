/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.se.raspberrypi.controller;

import deu.se.raspberrypi.dto.PostDto;
import deu.se.raspberrypi.dto.PostUpdateDto;
import deu.se.raspberrypi.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 *
 * @author Haruki
 */
@Controller
@RequiredArgsConstructor
public class PageController {

    private final PostService postService;

    // 1. 게시글 작성 폼
    @GetMapping("/board/write")
    public String writeForm() {
        return "board/write";
    }

    // 2. 게시글 저장
    @PostMapping("/board/save")
    public String save(PostDto dto, HttpServletRequest request) {
        // Spring이 자동으로 dto 객체를 생성해서 넘겨줌
        //JSP의 <form>의 name 속성과 DTO의 필드명이 동일하면, Spring이 내부적으로 setter를 호출해서 DTO에 값을 자동 주입함
        postService.save(dto, request);
        return "redirect:/board/list";
    }

    // 3. 게시글 리스트 조회
    @GetMapping("/board/list")
    public String list(Model model) {
        model.addAttribute("postList", postService.findAll());
        return "board/list";
    }

    // 4. 게시글 단일 조회
    @GetMapping("/board/view/{id}")
    public String view(@PathVariable Long id, Model model) {
        PostDto post = postService.findById(id);
        model.addAttribute("post", post);
        return "board/view";  // => view.jsp 로 forward
    }

    // 5. 게시글 수정 폼
    @GetMapping("/board/updateForm/{id}")
    public String updateForm(@PathVariable Long id, Model model) {
        PostDto post = postService.findById(id);
        model.addAttribute("post", post);
        return "board/update";
    }

    // 6. 게시글 수정 요청
    @PostMapping("/board/update/{id}")
    public String updatePost(@PathVariable Long id, PostUpdateDto dto) {
        postService.updateWithFiles(id, dto);
        return "redirect:/board/view/" + id;
    }

    // 7. 게시글 삭제 요청
    @PostMapping("/board/delete/{id}")
    public String deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return "redirect:/board/list";
    }
}
