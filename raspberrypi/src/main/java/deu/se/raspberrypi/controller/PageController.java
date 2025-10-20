/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.se.raspberrypi.controller;

import deu.se.raspberrypi.dto.BoardPostDto;
import deu.se.raspberrypi.service.BoardPostService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Haruki
 */
@Controller
public class PageController {

    @Autowired
    BoardPostService boardPostService;

    // 1. 게시글 작성
    @GetMapping("/board/write")
    public String write() {
        return "/board/write";
    }

    // 2. 게시글 저장
    @PostMapping("/board/save")
    public String save(BoardPostDto dto, HttpServletRequest request) { // Spring이 자동으로 dto 객체를 생성해서 넘겨줌
        //JSP의 <form>의 name 속성과 DTO의 필드명이 동일하면, Spring이 내부적으로 setter를 호출해서 DTO에 값을 자동 주입함
        boardPostService.save(dto, request);
        return "redirect:/board/list";
    }

    // 3. 게시글 리스트 조회
    @GetMapping("/board/list")
    public String list(Model model) {
        model.addAttribute("postList", boardPostService.findAll());
        return "/board/list";
    }

    // 4. 게시글 단일 조회
    @GetMapping("/board/view")
    public String view(@RequestParam("id") Long id, Model model) {
        BoardPostDto post = boardPostService.findById(id);
        model.addAttribute("post", post);
        return "board/view";  // => view.jsp 로 forward
    }
}
