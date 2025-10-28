/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.se.raspberrypi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 *
 * @author Haruki
 */
@Controller
public class MemberController {

    @GetMapping("/member/login")
    public String loginForm() {
        return "/member/login";
    }

    @GetMapping("/member/signup")
    public String signupForm() {
        return "/member/signup";
    }
}
