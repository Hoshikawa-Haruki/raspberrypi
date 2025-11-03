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
    
    @GetMapping("/member/login")
    public String loginForm() {
        return "member/login";
    }

    @GetMapping("/member/signup")
    public String signupForm() {
        return "member/signup";
    }

    @PostMapping("/member/signup")
    public String signup(@Valid SignupRequestDto dto) {
        memberService.register(dto);
        return "redirect:/member/login?signupSuccess";
    }
}
