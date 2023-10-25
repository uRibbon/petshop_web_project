package com.dog.shop.api.controller;

import com.example.test12.api.service.KakaoApiService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class KakaoApiController {

    private final KakaoApiService kakaoApiService;

    public KakaoApiController(KakaoApiService kakaoApiService) {
        this.kakaoApiService = kakaoApiService;
    }

    @GetMapping("/")
    public String index() {
        return "direction-api";
    }

    @PostMapping("/calculate-distance")
    public String calculateDistance(@RequestParam String address, Model model) {
        String result = kakaoApiService.calculateDistance(address);
        model.addAttribute("distance", result);
        return "direction-api";
    }
}
