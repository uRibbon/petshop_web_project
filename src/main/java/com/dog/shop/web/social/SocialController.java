package com.dog.shop.web.social;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SocialController {
    private final static String KAKAO_AUTH_URI = "https://kauth.kakao.com";
    private final static String KAKAO_API_URI = "https://kapi.kakao.com";

    @GetMapping("/kakao")
    public String getKakaoLogin() {
        return KAKAO_AUTH_URI + "/oauth/authorize"
                + "?client_id=" + "0995812abee8d29ce73b3bfb49440747"
                + "&redirect_uri=" + "http://localhost:8080/kakao/callback"
                + "&response_type=code";
    }

    @GetMapping("/kakao/callback")
    public ResponseEntity<?> callback(HttpServletRequest request) throws Exception {
        String code = request.getParameter("code");

        return ResponseEntity.ok().body(code);
    }

}
