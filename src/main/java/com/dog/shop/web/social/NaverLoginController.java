package com.dog.shop.web.social;

import com.dog.shop.domain.User;
import com.dog.shop.dto.userDto.UserReqDto;
import com.dog.shop.myenum.Role;
import com.dog.shop.service.AuthService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.Base64;

@RestController
public class NaverLoginController {

    @Autowired
    private AuthService authService;

    private static final String AUTH_URL = "https://nid.naver.com/oauth2.0/authorize";
    private static final String RESPONSE_TYPE = "code";
    private static final String STATE_SESSION_KEY = "OAUTH2_STATE";

    private String clientId = "gKBIU9kjlGIFKmj9nI7t";  // TODO: Replace with your client ID
    private String redirectUri = "http://localhost:8080/auth/callback";  // TODO: Replace with your redirect URI

    @GetMapping("/naver-login")
    public ResponseEntity<String> getNaverAuthenticationUrl(HttpSession session) {
        String stateToken = generateStateToken();
        session.setAttribute(STATE_SESSION_KEY, stateToken);

        URI uri = UriComponentsBuilder.fromUriString(AUTH_URL)
                .queryParam("response_type", RESPONSE_TYPE)
                .queryParam("client_id", clientId)
                .queryParam("redirect_uri", redirectUri)
                .queryParam("state", stateToken)
                .build()
                .toUri();

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);

        return response;
    }

    @GetMapping("/auth/callback")
    public String oauth2Callback(@RequestParam String state, @RequestParam String code, HttpSession session) {
        // 1. 상태 토큰 검증
        String storedState = (String) session.getAttribute(STATE_SESSION_KEY);
        if (storedState == null || !storedState.equals(state)) {
            return "Invalid state token! Possible CSRF attack.";
        }

        // 2. 액세스 토큰 요청
        String accessToken = requestAccessToken(code);

        if (accessToken == null) {
            return "Failed to obtain access token.";
        }

        // 3. 액세스 토큰 저장 (예제에서는 세션에 저장)
        session.setAttribute("NAVER_ACCESS_TOKEN", accessToken);

        String userProfileJson = getUserProfile(accessToken);

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode rootNode = objectMapper.readTree(userProfileJson);

            String resultcode = rootNode.path("resultcode").asText();
            String message = rootNode.path("message").asText();

            if ("00".equals(resultcode) && "success".equals(message)) {
                JsonNode responseNode = rootNode.path("response");
                String id = responseNode.path("id").asText();
                String email = responseNode.path("email").asText();
                String mobile = responseNode.path("mobile").asText();
                String name = responseNode.path("name").asText();
                String birthday = responseNode.path("birthday").asText();
                String birthyear = responseNode.path("birthyear").asText();

                // TODO: 이 정보들을 사용하여 필요한 처리 수행
                String password = "P@ssw0rd1";

                User user = new User();
                user.setRole(Role.USER);
                user.setPassword(password);
                user.setOAuthProvider("KAKAO");
                user.setEmail(email);
                LocalDate birthDate = LocalDate.parse(birthyear + "-" + birthday);
                user.setBirthDate(birthDate);
                user.setName(name);
                user.setPhoneNumber(mobile.replaceAll("-", ""));


                // TODO OAuth id도 넣어줘야함
                ModelMapper modelMapper1 = new ModelMapper();
                UserReqDto userReqDto = modelMapper1.map(user, UserReqDto.class);
                authService.signUser(userReqDto);

                return "Success! ID: " + id + ", Email: " + email + ", Mobile: " + mobile + ", Name: " + name + ", Birthday: " + birthday + ", Birthyear: " + birthyear;
            } else {
                return "Error! Resultcode: " + resultcode + ", Message: " + message;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Failed to parse user profile.";
        }
    }

    private String requestAccessToken(String code) {
        RestTemplate restTemplate = new RestTemplate();

        // TODO: Set the correct token endpoint URL and parameters for Naver
        String tokenEndpointUrl = "https://nid.naver.com/oauth2.0/token";
        MultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("client_id", clientId);
        // TODO: Add your client secret
        requestParams.add("client_secret", "lTpPaZgZOi");
        requestParams.add("grant_type", "authorization_code");
        requestParams.add("code", code);
        requestParams.add("redirect_uri", redirectUri);

        ResponseEntity<String> response = restTemplate.postForEntity(tokenEndpointUrl, requestParams, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode rootNode = objectMapper.readTree(response.getBody());
            return rootNode.path("access_token").asText();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    private String generateStateToken() {
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[32];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private String getUserProfile(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        String profileEndpointUrl = "https://openapi.naver.com/v1/nid/me";
        ResponseEntity<String> response = restTemplate.exchange(profileEndpointUrl, HttpMethod.GET, entity, String.class);

        return response.getBody();
    }
}