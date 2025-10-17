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

/**
 *
 * @author Haruki
 */
@Controller
public class PageController {

    @Autowired
    BoardPostService boardPostService;

    @GetMapping("/board/write") // 작성 페이지 이동
    public String writeForm() {
        return "/board/write";
    }

    //JSP의 <form>의 name 속성과 DTO의 필드명이 동일하면, Spring이 내부적으로 setter를 호출해서 DTO에 값을 자동 주입함
    @PostMapping("/board/save") // 게시글 작성+저장 요청
    public String save(BoardPostDto dto, HttpServletRequest request) { // Spring이 자동으로 dto 객체를 생성해서 넘겨줌
        boardPostService.save(dto, request);
        return "redirect:/board/list";
    }

    @GetMapping("/board/list")
    public String list(Model model) {
        model.addAttribute("posts", boardPostService.findAll());
        return "/board/list";
    }
}
