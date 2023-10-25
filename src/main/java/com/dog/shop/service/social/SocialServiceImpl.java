package com.dog.shop.service.social;

import com.dog.shop.dto.social.KakaoTokenResponse;
import com.dog.shop.dto.social.UserRegistrationOAuthData;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class SocialServiceImpl implements SocialService {


    @Override
    public void processKakaoOAuth(UserRegistrationOAuthData userRegistrationOAuthData) {
// 사용자 정보를 가져오기 위한 HTTP 요청 설정
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + userRegistrationOAuthData.getAccessToken());
        HttpEntity<String> request = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();

        // 카카오 API에서 사용자 정보 받아오기
        ResponseEntity<KakaoTokenResponse> response = restTemplate.exchange("https://kapi.kakao.com/v2/user/me",
                HttpMethod.GET, request, KakaoTokenResponse.class);
        KakaoTokenResponse kakaoTokenResponse = response.getBody();

        System.out.println(kakaoTokenResponse.toString());
    }
}
