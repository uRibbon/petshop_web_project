package com.dog.shop.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import java.io.IOException;

public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
                                Authentication authentication) throws IOException, ServletException {
        // JWT-TOKEN 쿠키 삭제
        Cookie jwtTokenCookie = new Cookie("JWT-TOKEN", null);
        jwtTokenCookie.setMaxAge(0); // 쿠키를 바로 만료
        jwtTokenCookie.setPath("/"); // 쿠키의 유효 경로 설정
        response.addCookie(jwtTokenCookie);

        // 로그아웃 후 리디렉션 또는 다른 로직
        response.sendRedirect("/");
    }
}