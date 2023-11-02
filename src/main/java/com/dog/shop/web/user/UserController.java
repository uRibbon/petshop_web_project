package com.dog.shop.web.user;

import com.dog.shop.domain.User;
import com.dog.shop.repository.UserRepository;
import com.dog.shop.utils.JwtUtil;
import com.dog.shop.help.JwtHelper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.Optional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final JwtUtil jwtUtil;
    private final JwtHelper jwtHelper;

    @Autowired
    private UserRepository userRepository;
    // 먼저 회원정보를 보여주고 그 이후에 업데이트 수정으로 넘어가게 변경
    @GetMapping("/user-detail")
        public String userDetail(Model model,HttpServletRequest request) throws IOException {


        String token = jwtHelper.extractTokenFromCookies(request);
            Optional<User> userOpt = jwtHelper.extractUserFromToken(token);
            Long userId = userOpt.get().getId();

            User user = userRepository.findById(userId).orElseThrow();
            model.addAttribute("user", user);

            return "user-detail";
    }

    @GetMapping("/updateInfo/page/{id}")
    public String userInfoPage(@PathVariable Long id,Model model) {
        // TODO 에러처리 필요
        User user = userRepository.findById(id).orElseThrow();
        model.addAttribute("user", user);
        return "updateInfo";
    }

}
