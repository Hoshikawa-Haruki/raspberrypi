/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.se.raspberrypi.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 *
 * @author Haruki
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 인가 정책
                .authorizeHttpRequests(auth -> auth
                // 정적 리소스 & 공개 페이지
                .requestMatchers(
                        "/", "/error",
                        "/css/**", "/js/**", "/images/**",
                        "/upload/**",
                        "/rappi_favicon.png",
                        "/member/loginForm", "/member/signupForm", // 로그인, 회원가입 페이지 요청
                        "/login", "/member/signup", // 회원가입, 로그인 요청
                        "/board", "/board/", "/board/list", "/board/view/**"
                ).permitAll()
                .requestMatchers("/board/delete/**").authenticated()
                // 게시판 나머지 = 로그인 필요
                .requestMatchers("/board/**").hasRole("USER")
                // 관리자 페이지
                .requestMatchers("/admin/**").hasRole("ADMIN")
                // 기타 요청은 인증 필요
                .anyRequest().authenticated()
                )
                // 폼 로그인
                .formLogin(form -> form
                .loginPage("/member/loginForm") // GET/loginForm 처리
                .loginProcessingUrl("/login") // POST/login 처리
                .usernameParameter("email")
                .passwordParameter("password")
                .defaultSuccessUrl("/board/list", true) // 로그인 성공 후 이동 페이지
                .permitAll()
                )
                // 로그아웃
                .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                );

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public org.springframework.security.web.firewall.HttpFirewall allowUrlEncodedDoubleSlashFirewall() {
        org.springframework.security.web.firewall.StrictHttpFirewall firewall
                = new org.springframework.security.web.firewall.StrictHttpFirewall();
        firewall.setAllowUrlEncodedDoubleSlash(true);
        firewall.setAllowBackSlash(true);
        firewall.setAllowSemicolon(true);
        return firewall;
    }

    @Bean
    public org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web
                .httpFirewall(allowUrlEncodedDoubleSlashFirewall())
                .ignoring()
                .requestMatchers("/WEB-INF/**");  // JSP forward 경로 무시 (무한 리디렉션 방지)
    }

    // 명시적 Provider (선택): UserDetailsService + Encoder 연결
    // TODO(2025-10-31, Haruki): DaoAuthenticationProvider 명시 필요 이유 아직 완전 이해 안 됨.
//    @Bean
//    public DaoAuthenticationProvider authenticationProvider(CustomUserDetailsService customUserDetailsService) {
//        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
//        provider.setUserDetailsService(customUserDetailsService);
//        provider.setPasswordEncoder(passwordEncoder());
//        return provider;
//    }
}
