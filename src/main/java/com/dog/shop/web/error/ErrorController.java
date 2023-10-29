package com.dog.shop.web.error;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController {

    @GetMapping("/unauthorized")
    public String unauthorized() {
        return "401";  // Thymeleaf template name
    }
}