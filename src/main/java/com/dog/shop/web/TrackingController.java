package com.dog.shop.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
public class TrackingController {

    @GetMapping("/trackParcel")
    public String trackParcel(@RequestParam String t_invoice) {
        RestTemplate restTemplate = new RestTemplate();
        String trackingUrl = "http://info.sweettracker.co.kr/api/v1/trackingInfo";
        
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(trackingUrl)
                .queryParam("t_key", "R5R7h5Iw6IoSyY0X3TN5Ug")
                .queryParam("t_code", "04")
                .queryParam("t_invoice", t_invoice);
        
        ResponseEntity<String> response = restTemplate.getForEntity(builder.toUriString(), String.class);

        // JSON 응답을 Java 객체로 변환
        // 예: TrackingInfo trackingInfo = objectMapper.readValue(response.getBody(), TrackingInfo.class);

        // 변환된 Java 객체를 Thymeleaf 템플릿에 전달하여 HTML 렌더링
        // 예: return "trackingPage"; (trackingPage는 Thymeleaf 템플릿 이름)

        // 여기서는 단순히 JSON 문자열을 반환
        return response.getBody();
    }
}