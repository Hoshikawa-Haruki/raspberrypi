/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.se.raspberrypi.security;

import deu.se.raspberrypi.entity.Member;
import java.util.Collection;
import java.util.List;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Spring Security 인증용 사용자 정보 클래스.
 *
 * - Member 엔티티를 기반으로 UserDetails 표준 인터페이스 구현 - 인증 후 SecurityContext에 저장되어
 * 세션(User Principal)로 활용됨 - 역할(ROLE_USER / ROLE_ADMIN) 및 계정 상태(활성/잠금 등) 전달
 *
 * 2025.10.30.
 *
 * @author Haruki
 */
@Getter
public class CustomUserDetails implements UserDetails {

    private final Long memberId;
    private final String nickName;
    private final String email;
    private final String password;
    private final String role;
    private final String status;

    private CustomUserDetails(
            Long memberId,
            String nickName,
            String email,
            String password,
            String role,
            String status
    ) {
        this.memberId = memberId;
        this.nickName = nickName;
        this.email = email;
        this.password = password;
        this.role = role;
        this.status = status;
    }

    // Member → 인증 스냅샷 변환
    // 로그인 '그 순간'의 사용자 상태를 복사해 둔 고정본
    public static CustomUserDetails from(Member member) {
        return new CustomUserDetails(
                member.getId(),
                member.getNickname(),
                member.getEmail(),
                member.getPassword(),
                member.getRole(),
                member.getStatus()
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !"BANNED".equalsIgnoreCase(status);
    }

    @Override
    public boolean isEnabled() {
        return "ACTIVE".equalsIgnoreCase(status);
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
}
