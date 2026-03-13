/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.se.raspberrypi.controller;

import deu.se.raspberrypi.dto.PortfolioListDto;
import deu.se.raspberrypi.dto.PortfolioViewDto;
import deu.se.raspberrypi.dto.PortfolioSaveRequestDto;
import deu.se.raspberrypi.dto.PortfolioUpdateDto;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 *
 * @author Haruki
 */
@Controller
@RequiredArgsConstructor
public class PortfolioController {

    private final PortfolioService portfolioService;

    // 0. 포트폴리오 리스트 조회
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

    // 3. 포트폴리오 단일 조회
    @GetMapping("/portfolio/{id}")
    public String viewPortfolio(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails user,
            Model model) {
        PortfolioViewDto portfolio = portfolioService.getPortfolioById(id);
        model.addAttribute("post", portfolio);
        Long loginMemberId = (user != null) ? user.getMemberId() : null;
        model.addAttribute("loginMemberId", loginMemberId);

        return "portfolio/view_portfolio";  // => view_portfolio.jsp 로 forward
    }

    // 4. 포트폴리오 수정 폼
    @GetMapping("/portfolio/updateForm/{id}")
    public String updateForm(@PathVariable Long id, Model model) {
        PortfolioViewDto dto = portfolioService.getPortfolioById(id);
        model.addAttribute("post", dto);
        return "portfolio/update_portfolio";
    }

    // 5. 포트폴리오 수정 요청
    @PostMapping("/portfolio/update/{id}")
    public String updatePortfolio(@PathVariable Long id,
            PortfolioUpdateDto dto,
            @AuthenticationPrincipal CustomUserDetails user) {
        portfolioService.update(id, dto, user.getMemberId());
        return "redirect:/portfolio/" + id;
    }

    // 6. 포트폴리오 삭제 요청
    @PostMapping("/portfolio/delete/{id}")
    public String deletePortfolio(@PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails user) {
        portfolioService.delete(id);
        return "redirect:/portfolio";
    }

}
