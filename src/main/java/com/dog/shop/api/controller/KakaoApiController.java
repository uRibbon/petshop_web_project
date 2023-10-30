package com.dog.shop.api.controller;

import com.dog.shop.api.service.KakaoApiService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/api")
public class KakaoApiController {

    private final KakaoApiService kakaoApiService;

    public KakaoApiController(KakaoApiService kakaoApiService) {
        this.kakaoApiService = kakaoApiService;
    }

    @GetMapping("/index")
    public String index() {
        return "direction-api";
    }

    @PostMapping("/calculate-distance")
    public ResponseEntity<Map<String, String>> calculateDistance(@RequestParam String address, RedirectAttributes redirectAttributes) {

        String result = kakaoApiService.calculateDistance(address, redirectAttributes);
        Map<String, String> responseMap = new HashMap<>();
        responseMap.put("distance", result);

        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }
}
