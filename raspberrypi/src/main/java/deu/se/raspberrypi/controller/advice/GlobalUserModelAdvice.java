/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.se.raspberrypi.controller.advice;

import deu.se.raspberrypi.config.FileProperties;
import deu.se.raspberrypi.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 *
 * @author Haruki
 */
@ControllerAdvice
@RequiredArgsConstructor
public class GlobalUserModelAdvice {

    private final FileProperties fileProperties;

    @ModelAttribute
    public void addLoginUserInfo(
            Model model,
            @AuthenticationPrincipal CustomUserDetails principal
    ) {
        if (principal != null) {
            model.addAttribute("loginUserNickname", principal.getNickName());
            model.addAttribute("loginMemberId", principal.getMemberId());
            model.addAttribute("loginUserRole", principal.getRole());
        }

        model.addAttribute("maxSingleImageSize", fileProperties.getMaxSingleImageSize().toBytes());
        model.addAttribute("maxInlineImageSize", fileProperties.getMaxInlineImageSize().toBytes());
        model.addAttribute("maxAttachmentSize", fileProperties.getMaxAttachmentSize().toBytes());
    }
}
