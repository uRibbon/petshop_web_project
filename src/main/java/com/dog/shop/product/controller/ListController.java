package com.dog.shop.product.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ListController {

    @GetMapping("/hello")
    public String List() {
        return"redirect:/products/list";
    }

}
