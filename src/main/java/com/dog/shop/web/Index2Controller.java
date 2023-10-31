package com.dog.shop.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Index2Controller {

    @GetMapping("/index2")
    public String index2() {
        return "index2";
    }
}
