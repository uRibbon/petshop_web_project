package com.dog.shop.web.mypage;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;

@Controller
public class MyPageController {

    @GetMapping("/myPage")
    public String myPage(HttpServletResponse response) throws IOException  {

        // 사용자가 로그인하지 않은 경우, 로그인 페이지로 리다이렉트 로그인 토큰정보로 비로그인 장바구니 선택시 에러처리
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken)  {
            response.sendRedirect("/login");
            return null;
        }

        return "myPage";
    }


}
