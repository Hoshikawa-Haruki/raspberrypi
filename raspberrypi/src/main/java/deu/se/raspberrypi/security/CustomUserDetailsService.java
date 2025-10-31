/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.se.raspberrypi.security;

import deu.se.raspberrypi.entity.Member;
import deu.se.raspberrypi.repository.MemberRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

/**
 * 사용자 인증 시 Spring Security가 DB에서 사용자 정보를 조회하는 서비스 클래스.
 * Security 인증 과정에서 이 클래스를 호출해 사용자 정보를 가져가고,
 * 이후 UserDetails(Principal) 형태로 세션(SecurityContext)에 저장
 * 
 * 2025.10.30
 * @author Haruki
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Member> opt = memberRepository.findByEmail(email);
        Member member = opt.orElseThrow(() -> new UsernameNotFoundException("No Such user: " + email));
        return new CustomUserDetails(member);
    }
}
