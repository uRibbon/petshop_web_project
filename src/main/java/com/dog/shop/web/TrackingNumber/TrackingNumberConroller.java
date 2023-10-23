package com.dog.shop.web.TrackingNumber;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/Order")
public class TrackingNumberConroller {

    @GetMapping("/info")
    public String info(){
        return "info";
    }


}
