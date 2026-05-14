package deu.se.raspberrypi.service;

/**
 * 관리자 기능 서비스
 * 회원 상태 변경(정지/해제/강제탈퇴) 및 회원 목록/통계 조회 처리
 *
 * 2026.05.14.
 *
 * @author Haruki
 */

import deu.se.raspberrypi.dto.MemberManageDto;
import deu.se.raspberrypi.entity.Member;
import deu.se.raspberrypi.repository.MemberRepository;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminService {

    private final MemberRepository memberRepository;

    // 상단 현황판용 상태별 회원 수 집계
    public Map<String, Long> getMemberStats() {
        Map<String, Long> stats = new LinkedHashMap<>();
        stats.put("total",   memberRepository.count());
        stats.put("active",  memberRepository.countByStatus("ACTIVE"));
        stats.put("banned",  memberRepository.countByStatus("BANNED"));
        stats.put("deleted", memberRepository.countByStatus("DELETED"));
        return stats;
    }

    // 전체 회원 목록 (가입일 최신순)
    public Page<MemberManageDto> getMemberList(Pageable pageable) {
        return memberRepository.findAllByOrderByCreatedAtDesc(pageable)
                .map(MemberManageDto::from);
    }

    // 상태별 회원 목록 (v2 탭 전환용)
    public Page<MemberManageDto> getMembersByStatus(String status, Pageable pageable) {
        return memberRepository.findByStatusOrderByCreatedAtDesc(status, pageable)
                .map(MemberManageDto::from);
    }

    // 정지/탈퇴 회원 목록 (처리일 최신순)
    public Page<MemberManageDto> getInactiveMembers(Pageable pageable) {
        return memberRepository.findByStatusInOrderByUpdatedAtDesc(List.of("BANNED", "DELETED"), pageable)
                .map(MemberManageDto::from);
    }

    // 회원 정지 처리
    @Transactional
    public void banMember(Long id) {
        findMember(id).ban();
    }

    // 정지 해제
    @Transactional
    public void unbanMember(Long id) {
        findMember(id).unban();
    }

    // 강제 탈퇴 (soft delete)
    @Transactional
    public void forceWithdraw(Long id) {
        findMember(id).softDelete();
    }

    // 공통 회원 조회 (없으면 예외)
    private Member findMember(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));
    }
}
