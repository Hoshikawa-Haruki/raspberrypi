/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.se.raspberrypi.controller;

import deu.se.raspberrypi.dto.PortfolioListDto;
import deu.se.raspberrypi.dto.PortfolioSaveRequestDto;
import deu.se.raspberrypi.security.CustomUserDetails;
import deu.se.raspberrypi.service.PortfolioService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 *
 * @author Haruki
 */
@Controller
@RequiredArgsConstructor
public class PortfolioController {

    private final PortfolioService portfolioService;

    @GetMapping("/portfolio")
    public String portfolioListPage(
            @PageableDefault(size = 9, sort = "createdAt",
                    direction = Sort.Direction.DESC) Pageable pageable,
            Model model) {

        Page<PortfolioListDto> page = portfolioService.getPortfolioList(pageable);

        model.addAttribute("portfolioPage", page);
        model.addAttribute("portfolioList", page.getContent());

        return "board/portfolio_list";
    }

    // 1. 게시글 작성 폼
    @GetMapping("/portfolio/writeForm")
    public String writeForm() {
        return "portfolio/write_portfolio";
    }

    // 2. 게시글 저장
    @PostMapping("/portfolio/save")
    public String savePortfolio(PortfolioSaveRequestDto dto,
            @AuthenticationPrincipal CustomUserDetails user) {
        portfolioService.save(dto, user.getMemberId());
        return "redirect:/portfolio";
    }

}
