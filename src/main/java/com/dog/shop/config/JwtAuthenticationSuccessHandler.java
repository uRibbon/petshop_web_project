package com.dog.shop.config;

import com.dog.shop.myenum.Role;
import com.dog.shop.utils.JwtUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;

/*    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String id = userDetails.getUsername(); // 일반적으로 UserDetails의 username은 이메일로 사용됩니다.

        GrantedAuthority authority = userDetails.getAuthorities().iterator().next();
        Role role = Role.valueOf(authority.getAuthority()); // String 형식의 권한 이름을 Role enum으로 변환

        // JWT 토큰 생성
        String token = jwtUtil.createAccessToken(id, role);


        // 토큰을 HTTP-only 쿠키로 설정
        Cookie jwtCookie = new Cookie("JWT-TOKEN", token);
        jwtCookie.setHttpOnly(true);
        response.addCookie(jwtCookie);

        // 기본 로그인 성공 핸들러 로직 실행
        try {
            super.onAuthenticationSuccess(request, response, authentication);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }*/



    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();

        GrantedAuthority authority = userDetails.getAuthorities().iterator().next();
        Role role = Role.valueOf(authority.getAuthority());

        // JWT 토큰 생성
        String token = jwtUtil.createAccessToken(email, role);
        System.out.println("token ========== "+token);
        // 토큰을 HTTP-only 쿠키로 설정
        Cookie jwtCookie = new Cookie("JWT-TOKEN", token);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setSecure(true); // Secure flag 설정
        jwtCookie.setMaxAge((int) jwtUtil.getTokenValidityInMilliseconds() / 1000); // 쿠키 만료 시간 설정 (JWT 토큰의 만료 시간과 동일하게 설정)

        response.addCookie(jwtCookie);

        // 직접 /index로 리다이렉션
        response.sendRedirect("/index");

        /*try {
            super.onAuthenticationSuccess(request, response, authentication);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/
    }
}