package com.dog.shop.web;

import com.dog.shop.domain.User;
import com.dog.shop.dto.userDto.UserReqDto;
import com.dog.shop.service.CheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/check")
public class CheckController {

//    @Autowired
//    private CheckService checkService;

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
//        model.addAttribute("agree", "Y");
        return "signup";
    }

//    @PostMapping("/user")
//    public String registerUser(UserReqDto userReqDto) {
//        checkService.agreeUser(userReqDto);
//       return "redirect:/check/signup";
//    }

}