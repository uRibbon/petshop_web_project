package com.dog.shop.web.user;

import com.dog.shop.domain.User;
import com.dog.shop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/updateInfo/page")
    public String userInfoPage(Model model) {
        Long userId = 2L;
        // TODO 에러처리 필요
        User user = userRepository.findById(userId).orElseThrow();
        model.addAttribute("user", user);
        return "updateInfo";
    }



}
