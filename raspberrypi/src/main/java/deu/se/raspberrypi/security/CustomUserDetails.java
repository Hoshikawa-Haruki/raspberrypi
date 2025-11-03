/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.se.raspberrypi.security;

import deu.se.raspberrypi.entity.Member;
import java.util.Collection;
import java.util.Collections;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Spring Security 인증용 사용자 정보 클래스.
 * 
 * - Member 엔티티를 기반으로 UserDetails 표준 인터페이스 구현
 * - 인증 후 SecurityContext에 저장되어 세션(User Principal)로 활용됨
 * - 역할(ROLE_USER / ROLE_ADMIN) 및 계정 상태(활성/잠금 등) 전달
 * 
 * 2025.10.30.
 * @author Haruki
 */
@Getter
public class CustomUserDetails implements UserDetails {

    private final Member member;

    public CustomUserDetails(Member member) {
        this.member = member; // 로그인한 사용자 정보를 보관
    }

    // 권한 반환
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(member.getRole()));
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public String getUsername() {
        // 로그인 ID = email
        return member.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !"BANNED".equalsIgnoreCase(member.getStatus());
    }

    @Override
    public boolean isEnabled() {
        return "ACTIVE".equalsIgnoreCase(member.getStatus());
    }
}
