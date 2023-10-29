package com.dog.shop.api.controller;

import com.dog.shop.api.service.KakaoApiService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String calculateDistance(@RequestParam String address, Model model, RedirectAttributes redirectAttributes) {
        String result = kakaoApiService.calculateDistance(address, redirectAttributes);
        model.addAttribute("distance", result);

        String fare = kakaoApiService.calculateDistance(address, redirectAttributes);
        model.addAttribute("fare", fare);

        return "cartItem";
    }
}
