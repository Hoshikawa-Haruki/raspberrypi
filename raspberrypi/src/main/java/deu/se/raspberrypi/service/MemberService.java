/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.se.raspberrypi.service;

import deu.se.raspberrypi.dto.MemberProfileDto;
import deu.se.raspberrypi.dto.SignupRequestDto;
import deu.se.raspberrypi.entity.Member;
import deu.se.raspberrypi.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Haruki
 */
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder; // BCrypt 암호화

    public void signup(SignupRequestDto dto) {

        // 비밀번호 일치 확인
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // 이메일 중복 체크
        if (memberRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        Member member = Member.builder()
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .nickname(dto.getNickname())
                .build();

        memberRepository.save(member);
    }

    // 마이페이지 사용자 정보 DTO 리턴
    public MemberProfileDto getProfile(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원정보가 존재하지 않습니다."));

        return MemberProfileDto.from(member);
    }

    public boolean existsEmail(String email) {
        return memberRepository.existsByEmail(email);
    }

    @Transactional
    public void withdraw(Long memberId, String password) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원정보가 존재하지 않습니다."));

        // 비밀번호 확인
        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // 회원 soft delete
        member.softDelete();
    }

    // 비밀번호 변경
    @Transactional
    public void changePassword(Long memberId, String currentPassword, String newPassword) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원정보가 존재하지 않습니다."));

        // 1. 현재 비밀번호 검증
        if (!passwordEncoder.matches(currentPassword, member.getPassword())) {
            throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.");
        }

        // 2. 기존 비밀번호와 동일한지 체크
        if (passwordEncoder.matches(newPassword, member.getPassword())) {
            throw new IllegalArgumentException("기존 비밀번호와 동일하게 설정할 수 없습니다.");
        }

        // 새 비밀번호 변경
        member.changePassword(passwordEncoder.encode(newPassword));
    }
}
