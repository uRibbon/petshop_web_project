package com.dog.shop.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TestConroller {

    @GetMapping("/api/register")
    @ResponseBody
    public String test() {
        return "test";
    }

    @GetMapping("/customLogin")
    public String index() {
        return "redirect:/index";
    }
ddddsadasd
}
