/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.se.raspberrypi.config;

import deu.se.raspberrypi.security.CustomAuthFailureHandler;
import deu.se.raspberrypi.security.CustomUserDetailsService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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

    private final CustomAuthFailureHandler customAuthFailureHandler;
    private final CustomUserDetailsService customUserDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // CSRF 예외 처리: 토스트 UI 이미지 업로드 전용
                // 이미지 업로드는 프론트 에디터용 API라 CSRF 예외처리가 표준. 2025.11.16.
                .csrf(csrf -> csrf
                .ignoringRequestMatchers("/upload/**")
                )
                // 인가 정책
                .authorizeHttpRequests(auth -> auth
                // 정적 리소스 & 공개 페이지
                .requestMatchers(
                        "/", "/error",
                        "/css/**", "/js/**", // static
                        "/images/**", // 정적이미지
                        "/upload/**",
                        "/upload_temp/**",
                        "/login", // 로그인 요청
                        "/board", "/board/", "/board/list", "/board/view/**",
                        "/profile", // 프로필 페이지,
                        "/portfolio",
                        "/portfolio/view/**"
                ).permitAll()
                .requestMatchers("/member/loginForm", "/member/signupForm", "/member/signup").anonymous() // 로그인, 회원가입 페이지 
                .requestMatchers("/member/security").authenticated() // 비밀번호 변경
                .requestMatchers(HttpMethod.GET, "/api/comments/**", "/api/member/check-email").permitAll() // 댓글 호출, 이메일 중복체크
                .requestMatchers(HttpMethod.GET, "/member/withdraw-success").permitAll()
                .requestMatchers(HttpMethod.GET, "/member/withdrawForm").authenticated()
                .requestMatchers(HttpMethod.POST, "/member/withdraw").authenticated()
                .requestMatchers(HttpMethod.POST, "/api/comments/**").authenticated()
                .requestMatchers(HttpMethod.DELETE, "/api/comments/**").authenticated()
                .requestMatchers(HttpMethod.DELETE, "/api/myposts/**").authenticated()
                .requestMatchers(HttpMethod.DELETE, "/api/posts/**").authenticated()
                .requestMatchers("/board/writeForm", "/board/updateForm/**", "/board/update/**", "/board/save").authenticated()
                .requestMatchers("/board/delete/**").authenticated()
                .requestMatchers("/admin/**").hasRole("ADMIN") // TODO : 관리자페이지
                // 기타 요청은 인증 필요
                .anyRequest().authenticated()
                )
                // API(/api/**) 요청에 대해서는 인증 실패 시
                // 로그인 페이지로 리다이렉트하지 않고 401 Unauthorized를 반환한다.
                .exceptionHandling(ex -> ex
                .authenticationEntryPoint((request, response, authException) -> {
                    if (request.getRequestURI().startsWith("/api/")) {
                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                    } else {
                        response.sendRedirect("/member/loginForm");
                    }
                })
                )
                // 폼 로그인
                .formLogin(form -> form
                .loginPage("/member/loginForm") // GET/loginForm 처리
                .loginProcessingUrl("/login") // POST/login 처리
                .usernameParameter("email")
                .passwordParameter("password")
                .defaultSuccessUrl("/board/list", true) // 로그인 성공 후 이동 페이지
                .failureHandler(customAuthFailureHandler) // 로그인 실패 처리 핸들러
                .permitAll()
                )
                .rememberMe(remember -> remember // 자동 로그인
                .rememberMeParameter("remember-me") // 체크박스 파라미터
                .key("my-remember-key-123") // 아무 문자열 (중요)
                .tokenValiditySeconds(60 * 60 * 24 * 7) // 7일
                .userDetailsService(customUserDetailsService)
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
