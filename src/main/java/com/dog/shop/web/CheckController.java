package com.dog.shop.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/check")
public class CheckController {

    //가입 취소
    @GetMapping("/index")
    public String index() {
        return "index";
    }

    //약관 페이지
    @GetMapping("/agree")
    public String showCheck() {
        return "agree-check";
    }

    //회원 가입 페이지
    @GetMapping("/signup")
    public String showSignup(Model model) {
        return "signup";
    }
}