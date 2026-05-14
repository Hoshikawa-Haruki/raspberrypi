package deu.se.raspberrypi.controller;

/**
 * 관리자 전용 컨트롤러
 * 회원 목록 조회, 정지/해제, 강제 탈퇴 등 회원 관리 기능 처리
 *
 * 2026.05.14.
 *
 * @author Haruki
 */

import deu.se.raspberrypi.dto.PaginationInfoDto;
import deu.se.raspberrypi.service.AdminService;
import deu.se.raspberrypi.util.PaginationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    // 관리자 회원 목록 페이지
    @GetMapping("/members")
    public String memberList(
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(defaultValue = "0") int inactivePage, // 우측 정지/탈퇴 목록 페이지 (좌측과 파라미터 분리)
            Model model) {

        var page = adminService.getMemberList(pageable);
        PaginationInfoDto pageInfo = PaginationUtils.of(page, 10);

        Pageable inactivePageable = PageRequest.of(inactivePage, 10, Sort.by("updatedAt").descending());
        var inactiveMemberPage = adminService.getInactiveMembers(inactivePageable);
        PaginationInfoDto inactivePageInfo = PaginationUtils.of(inactiveMemberPage, 5);

        model.addAttribute("stats", adminService.getMemberStats());
        model.addAttribute("memberPage", page);
        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("inactiveMemberPage", inactiveMemberPage);
        model.addAttribute("inactivePageInfo", inactivePageInfo);

        return "admin/member_list";
    }

    // 관리자 회원 목록 페이지 v2 (탭 전환 방식)
    @GetMapping("/members/v2")
    public String memberListV2(
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(defaultValue = "all") String tab,
            Model model) {

        var page = switch (tab) {
            case "banned"  -> adminService.getMembersByStatus("BANNED", pageable);
            case "deleted" -> adminService.getMembersByStatus("DELETED", pageable);
            default        -> adminService.getMemberList(pageable);
        };
        PaginationInfoDto pageInfo = PaginationUtils.of(page, 10);

        model.addAttribute("stats", adminService.getMemberStats());
        model.addAttribute("memberPage", page);
        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("tab", tab);

        return "admin/member_list_v2";
    }

    // 회원 정지
    @PostMapping("/members/{id}/ban")
    @ResponseBody
    public ResponseEntity<Void> banMember(@PathVariable Long id) {
        adminService.banMember(id);
        return ResponseEntity.ok().build();
    }

    // 정지 해제
    @PostMapping("/members/{id}/unban")
    @ResponseBody
    public ResponseEntity<Void> unbanMember(@PathVariable Long id) {
        adminService.unbanMember(id);
        return ResponseEntity.ok().build();
    }

    // 강제 탈퇴
    @PostMapping("/members/{id}/withdraw")
    @ResponseBody
    public ResponseEntity<Void> forceWithdraw(@PathVariable Long id) {
        adminService.forceWithdraw(id);
        return ResponseEntity.ok().build();
    }
}
