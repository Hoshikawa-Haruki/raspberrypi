/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.se.raspberrypi.controller;

import deu.se.raspberrypi.dto.SignupRequestDto;
import deu.se.raspberrypi.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 *
 * @author Haruki
 */
@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/member/loginForm")
    public String loginForm() {
        return "member/login";
    }

    @GetMapping("/member/signupForm")
    public String signupForm() {
        return "member/signup";
    }

    @PostMapping("/member/signup")
    public String signup(@Valid SignupRequestDto dto, BindingResult bindingResult, Model model) {

        // 1) Bean Validation 실패 처리
        if (bindingResult.hasErrors()) {
            model.addAttribute("errorMessage",
                    bindingResult.getAllErrors().get(0).getDefaultMessage());
            return "member/signup";
        }

        // 2) 비밀번호 일치 확인
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            model.addAttribute("errorMessage", "비밀번호가 일치하지 않습니다.");
            return "member/signup";
        }
        try {
            memberService.register(dto);
            return "redirect:/member/loginForm?signupSuccess";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "member/signup"; // 다시 회원가입 페이지로 돌아감
        }
    }
}
