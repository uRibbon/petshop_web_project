package com.dog.shop.web;

import com.dog.shop.dto.userDto.UserReqDto;
import com.dog.shop.dto.userDto.UserResDto;
import com.dog.shop.exception.MemberNotFoundException;
import com.dog.shop.service.AuthService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Slf4j
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @GetMapping("/signup")
    public String signup() {

        return "signup";
    }

    @GetMapping("/check")
    public ResponseEntity<Boolean> memCheck(@RequestParam String email) {
        System.out.println("이메일 확인 : " + email);
        boolean result = authService.userCheck(email);
        System.out.println(result);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/signup")
    public String saveUser(UserReqDto userReqDto, HttpSession session) {

        String chkTerms = (String) session.getAttribute("chkTerms");
        String chkPrivacy = (String) session.getAttribute("chkPrivacy");
        //char chkMarketing = (char) session.getAttribute("chkMarketing");
        Object chkTermsObj = session.getAttribute("chkMarketing");
        String chkMarketing = (chkTermsObj != null) ? (String) chkTermsObj : "N"; // 'N'은 기본값으로 사용됩니다.
        userReqDto.setChkTerms(chkTerms);
        userReqDto.setChkPrivacy(chkPrivacy);
        userReqDto.setChkMarketing(chkMarketing);

        try {
            authService.signUser(userReqDto);
            System.out.println("컨트롤러 " + userReqDto.getEmail());
            return "redirect:/login"; // 회원가입 성공 시 로그인 페이지로 리다이렉트
        } catch (MemberNotFoundException e) {
            // 유효하지 않은 비밀번호로 회원가입 시도한 경우에 대한 예외 처리
            return "redirect:/signup?error=invalid_password"; // 유효하지 않은 비밀번호 오류 메시지를 URL 파라미터로 전달
        }
    }

    @GetMapping
    @ResponseBody
    public List<UserResDto> getUser() {

        return authService.getUser();
    }

    @PostMapping("/delete")
    public ResponseEntity<Boolean> userDelete( UserReqDto userReqDto) {
        String email = userReqDto.getEmail();
        boolean result = authService.userDelete(email);
        if (result) {
            return new ResponseEntity<>(true, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/update/{userId}")
    public Boolean updateUser(@PathVariable Long userId, UserReqDto userReqDto) {
        return authService.updateUserInfo(userId, userReqDto);
    }

    @PostMapping("/resetPwd/{email}")
    public String resetPwd(@PathVariable String email, UserReqDto userReqDto, Model model) {
        boolean result = authService.resetPwd(email, userReqDto);

        // 모델에 결과값을 담아서 템플릿에 전달
        model.addAttribute("resetResult", result);

        // 타임리프 템플릿의 경로를 리턴 (예: "resetPwdResult"는 타임리프 템플릿 파일의 경로)
        return "resetPwdResult";
    }


}