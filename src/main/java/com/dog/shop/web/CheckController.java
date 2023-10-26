package com.dog.shop.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CheckController {

    @GetMapping("/check")
    public String List() {
        return"check";
    }

}