package com.dog.shop.web.CartItem;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ListController {

    @GetMapping("/")
    public String index() {
        return"redirect:/products/list";
    }

}
