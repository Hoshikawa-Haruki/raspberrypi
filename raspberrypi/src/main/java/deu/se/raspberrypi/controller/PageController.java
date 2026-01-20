/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.se.raspberrypi.controller;

import deu.se.raspberrypi.dto.PostDto;
import deu.se.raspberrypi.dto.PostListDto;
import deu.se.raspberrypi.dto.PostUpdateDto;
import deu.se.raspberrypi.security.CustomUserDetails;
import deu.se.raspberrypi.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
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
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * 2025.12.25. PostController 로 수정 고려
 *
 * @author Haruki
 */
@Controller
@RequiredArgsConstructor
public class PageController {

    private final PostService postService;

    // 1. 게시글 작성 폼
    @GetMapping("/board/writeForm")
    public String writeForm() {
        return "board/write_toastui_ver2";
    }

    // 2. 게시글 저장
    @PostMapping("/board/save")
    public String save(PostDto dto,
            @AuthenticationPrincipal CustomUserDetails user,
            HttpServletRequest request) {
        // Spring이 자동으로 dto 객체를 생성해서 넘겨줌
        //JSP의 <form>의 name 속성과 DTO의 필드명이 동일하면, Spring이 내부적으로 setter를 호출해서 DTO에 값을 자동 주입함
        postService.save(dto, user.getMember(), request); // Spring Security가 세션에 넣어둔 로그인 Member 엔티티 반환
        return "redirect:/board/list";
    }

    // 3. 게시글 리스트 조회
    //    @GetMapping("/board/list")
    //    public String list(Model model) {
    //        model.addAttribute("postList", postService.findAll());
    //        return "board/list";
    //    }
    // 3.1. 게시글 리스트 페이지 자료형 조회 (LEGACY)
    //@GetMapping("/board/list")
    @Deprecated
    public String list(
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            Model model
    ) {
        Page<PostListDto> postPage = postService.findAllPage(pageable);

        int currentPage = postPage.getNumber();
        int totalPages = postPage.getTotalPages();

        int startPage = (currentPage / 10) * 10;
        int endPage = Math.min(startPage + 9, totalPages - 1);

        model.addAttribute("currentPage", currentPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("postPage", postPage); // 페이지 메타정보
        model.addAttribute("postList", postPage.getContent()); // 화면 출력용

        return "board/list";
    }

    // 3.2 게시글 리스트 페이지 자료형 조회 + 검색
    @GetMapping("/board/list")
    public String list(
            @RequestParam(required = false) String searchType,
            @RequestParam(required = false) String keyword,
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            Model model
    ) {

        // 입력값 정규화 (컨트롤러 책임)
        if (searchType == null || searchType.isBlank()) {
            searchType = "title_content";
        }

        if (keyword != null) {
            keyword = keyword.trim();
            if (keyword.isEmpty()) {
                keyword = null;
            }
        }

        Page<PostListDto> postPage
                = postService.searchPost(searchType, keyword, pageable);

        int currentPage = postPage.getNumber();
        int totalPages = postPage.getTotalPages();
        int startPage = 0;
        int endPage = -1; // 기본값 (페이지 없음)

        if (totalPages > 0) {
            startPage = (currentPage / 10) * 10;
            endPage = Math.min(startPage + 9, totalPages - 1);
        }

        model.addAttribute("currentPage", currentPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("postPage", postPage);
        model.addAttribute("postList", postPage.getContent());

        // 검색 상태 유지용 (중요)
        model.addAttribute("searchType", searchType);
        model.addAttribute("keyword", keyword);

        return "board/list";
    }

    // 4. 게시글 단일 조회
    @Deprecated
    // @GetMapping("/board/view/{id}")
    public String view(@PathVariable Long id, Model model) {
        PostDto post = postService.findById(id);
        model.addAttribute("post", post);
        return "board/view";  // => view.jsp 로 forward
    }

    // 4. 게시글 단일 조회 + 검색 페이지 유지 
    @GetMapping("/board/view/{id}")
    public String view(
            @PathVariable Long id,
            @RequestParam(required = false) String searchType,
            @RequestParam(required = false) String keyword,
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            Model model
    ) {
        // 1. 단일 게시글
        PostDto post = postService.findById(id);
        model.addAttribute("post", post);

        // 2. 검색 + 페이지 상태 유지
        Page<PostListDto> postPage
                = postService.searchPost(searchType, keyword, pageable);

        int currentPage = postPage.getNumber();
        int totalPages = postPage.getTotalPages();
        int startPage = 0;
        int endPage = -1;
        
        if (totalPages > 0) {
            startPage = (currentPage / 10) * 10;
            endPage = Math.min(startPage + 9, totalPages - 1);
        }

        // 3. pagination.jsp가 요구하는 값들 전부 전달
        model.addAttribute("postPage", postPage);
        model.addAttribute("postList", postPage.getContent());
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("totalPages", totalPages);

        model.addAttribute("searchType", searchType);
        model.addAttribute("keyword", keyword);
        model.addAttribute("currentPostId", id);

        return "board/view";
    }

    // 5. 게시글 수정 폼
    @GetMapping("/board/updateForm/{id}")
    public String updateForm(@PathVariable Long id, Model model) {
        PostDto post = postService.findById(id);
        model.addAttribute("post", post);
        return "board/update_toastui_ver2";
    }

    // 6. 게시글 수정 요청
    @PostMapping("/board/update/{id}")
    public String updatePost(@PathVariable Long id,
            PostUpdateDto dto,
            @AuthenticationPrincipal CustomUserDetails user) {
        postService.updateWithFiles(id, dto, user.getMember());
        return "redirect:/board/view/" + id;
    }

    // 7. 게시글 삭제 요청
    @PostMapping("/board/delete/{id}")
    public String deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return "redirect:/board/list";
    }
}
