package com.dog.shop.web;

import com.dog.shop.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/")
    public String showIndex(HttpServletRequest request, Model model) {
        // 1. JWT-TOKEN 쿠키를 가져옵니다.
        String jwtToken = getJwtFromCookie(request);

        if (jwtToken != null) {
            // 2. JWT 토큰에서 필요한 정보를 추출합니다.
            String userId = getEmailFromToken(jwtToken); // 이메일 추출 메서드는 직접 구현해야 합니다.

            // 3. 모델에 정보를 추가합니다.
            model.addAttribute("userId", userId);
        }

        return "index"; // 타임리프 템플릿 이름
    }

    private String getJwtFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("JWT-TOKEN".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    public String getEmailFromToken(String token) {
        try {
            Claims claims = jwtUtil.decode(token);
            return claims.get("id", String.class);
        } catch (Exception e) {
            // 토큰 파싱 중 오류 발생 시 null 반환 또는 적절한 예외 처리
            return null;
        }
    }
}
