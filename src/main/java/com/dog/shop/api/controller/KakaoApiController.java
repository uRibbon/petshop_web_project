package com.dog.shop.api.controller;

import com.dog.shop.api.service.KakaoApiService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/apii")
public class KakaoApiController {

    private final KakaoApiService kakaoApiService;

    public KakaoApiController(KakaoApiService kakaoApiService) {
        this.kakaoApiService = kakaoApiService;
    }

    @GetMapping("/index")
    public String index() {
        return "direction-api";
    }

    /*@PostMapping("/calculate-distance")
    public String calculateDistance(@RequestParam String address, Model model) {
        String result = kakaoApiService.calculateDistance(address);
        model.addAttribute("distance", result);
        return "direction-api";
    }*/

    @GetMapping("/calculate-distance")
    @ResponseBody
    public int calculateDistance(@RequestParam String address) {
        return kakaoApiService.calculateDistance(address);
    }
}
