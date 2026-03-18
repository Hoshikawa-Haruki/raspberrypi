/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.se.raspberrypi.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

/**
 *
 * @author Haruki
 */
@Component
public class CustomAuthFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception
    ) throws IOException, ServletException {

        String errorMessage = "로그인 실패";

        Throwable cause = exception.getCause(); //

        if (exception instanceof LockedException || cause instanceof LockedException) {
            errorMessage = "서비스 이용이 정지된 계정입니다.";
        } else if (exception instanceof DisabledException || cause instanceof DisabledException) {
            errorMessage = "탈퇴한 계정입니다.";
        } else if (exception instanceof BadCredentialsException || cause instanceof BadCredentialsException) {
            errorMessage = "이메일 또는 비밀번호가 올바르지 않습니다.";
        }

        // 메시지를 session에 저장
        request.getSession().setAttribute("loginErrorMessage", errorMessage);
        setDefaultFailureUrl("/member/loginForm?error=true"); // redirect

        super.onAuthenticationFailure(request, response, exception);
    }
}
