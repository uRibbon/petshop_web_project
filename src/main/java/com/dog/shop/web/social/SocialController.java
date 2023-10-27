package com.dog.shop.web.social;

import com.dog.shop.custom.CustomUserDetailService;
import com.dog.shop.domain.User;
import com.dog.shop.dto.social.OAuthToken;
import com.dog.shop.dto.userDto.UserReqDto;
import com.dog.shop.myenum.Role;
import com.dog.shop.service.AuthService;
import com.dog.shop.utils.JwtUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.ModelAndView;

import java.net.*;

@RestController
public class SocialController {
    private final static String KAKAO_AUTH_URI = "https://kauth.kakao.com";
    private final static String KAKAO_API_URI = "https://kauth.kakao.com";

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AuthService authService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailService customUserDetailService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/kakao")
    public String getKakaoLogin() {
        return KAKAO_AUTH_URI + "/oauth/authorize"
                + "?client_id=" + "0995812abee8d29ce73b3bfb49440747"
                + "&redirect_uri=" + "http://localhost:8080/kakao/callback"
                + "&response_type=code";
    }

    @GetMapping("/kakao/callback")
    public ModelAndView kakaoCallback(String code, HttpServletResponse httpServletResponse) throws JsonProcessingException, MalformedURLException { // Data를 리턴해주는 컨트롤러 함수

        // POST 방식으로 key=value 데이터를 요청 (카카오쪽으로)
        // 이 때 필요한 라이브러리가 RestTemplate, 얘를 쓰면 http 요청을 편하게 할 수 있다.
        RestTemplate rt = new RestTemplate();

        // HTTP POST를 요청할 때 보내는 데이터(body)를 설명해주는 헤더도 만들어 같이 보내줘야 한다.
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        // body 데이터를 담을 오브젝트인 MultiValueMap를 만들어보자
        // body는 보통 key, value의 쌍으로 이루어지기 때문에 자바에서 제공해주는 MultiValueMap 타입을 사용한다.
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", "0995812abee8d29ce73b3bfb49440747");
        params.add("redirect_uri", "http://localhost:8080/kakao/callback");
        params.add("code", code);

        // 요청하기 위해 헤더(Header)와 데이터(Body)를 합친다.
        // kakaoTokenRequest는 데이터(Body)와 헤더(Header)를 Entity가 된다.
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, headers);

        // POST 방식으로 Http 요청한다. 그리고 response 변수의 응답 받는다.
        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token", // https://{요청할 서버 주소}
                HttpMethod.POST, // 요청할 방식
                kakaoTokenRequest, // 요청할 때 보낼 데이터
                String.class // 요청 시 반환되는 데이터 타입
        );

        OAuthToken oAuthToken = null;

        try {
            oAuthToken = objectMapper.readValue(response.getBody(), OAuthToken.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        System.out.println("카카오 엑세스 토큰" + oAuthToken.getAccess_token());


        RestTemplate rt2 = new RestTemplate();

        // HTTP POST를 요청할 때 보내는 데이터(body)를 설명해주는 헤더도 만들어 같이 보내줘야 한다.
        HttpHeaders headers2 = new HttpHeaders();
        headers2.add("Authorization", "Bearer " + oAuthToken.getAccess_token());
        headers2.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // 요청하기 위해 헤더(Header)와 데이터(Body)를 합친다.
        // kakaoTokenRequest는 데이터(Body)와 헤더(Header)를 Entity가 된다.
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest2 = new HttpEntity<>(headers2);

        // POST 방식으로 Http 요청한다. 그리고 response 변수의 응답 받는다.
        ResponseEntity<String> response2 = rt2.exchange(
                "https://kapi.kakao.com/v2/user/me", // https://{요청할 서버 주소}
                HttpMethod.POST, // 요청할 방식
                kakaoTokenRequest2, // 요청할 때 보낼 데이터
                String.class // 요청 시 반환되는 데이터 타입
        );


        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(response2.getBody());

        long id = rootNode.get("id").asLong();
        String email = rootNode.path("kakao_account").get("email").asText();

        String password = "P@ssw0rd1";


        User user = new User();
        user.setRole(Role.USER);
        user.setPassword(password);
        user.setOAuthProvider("KAKAO");
        user.setEmail(email);
        // TODO OAuth id도 넣어줘야함
        ModelMapper modelMapper1 = new ModelMapper();
        UserReqDto userReqDto = modelMapper1.map(user, UserReqDto.class);
        authService.signUser(userReqDto);

        // JWT 액세스 토큰 생성
        String accessToken = jwtUtil.createAccessToken(email, Role.USER);

        // JWT를 이용한 Spring Security 인증
        UserDetails userDetails = customUserDetailService.loadUserByUsername(email);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 토큰을 HTTP-only 쿠키로 설정
        Cookie jwtCookie = new Cookie("JWT-TOKEN", accessToken);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setSecure(false); // For development. Consider setting this to true in production for HTTPS-only transmission.
        jwtCookie.setPath("/");     // Set the cookie for the entire application
        int expirationDuration = (int) jwtUtil.getTokenValidityInMilliseconds() / 1000;
        if (expirationDuration > 0) { // Ensure that the expiration time is valid
            jwtCookie.setMaxAge(expirationDuration);
        } else {
            // Handle or log an error about invalid token validity duration
        }

        httpServletResponse.addCookie(jwtCookie);

        ModelAndView modelAndView = new ModelAndView("index");
        return modelAndView;


    }

}

    


