package com.dog.shop.web;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/check")
public class CheckController {

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

    @PostMapping("/process-agreement")
    public String processAgreement(
            @RequestParam("chkTerms") boolean chkTerms,
            @RequestParam("chkPrivacy") boolean chkPrivacy,
            @RequestParam(value = "chkMarketing", defaultValue = "false") boolean chkMarketing,
            HttpSession session) {

        // boolean 값을 'Y' 또는 'N'으로 변환
        String chkTermsValue = chkTerms ? "Y" : "N";
        String chkPrivacyValue = chkPrivacy ? "Y" : "N";
        String chkMarketingValue = chkMarketing ? "Y" : "N";

        // 변환된 값들을 세션에 저장
        session.setAttribute("chkTerms", chkTermsValue);
        session.setAttribute("chkPrivacy", chkPrivacyValue);
        session.setAttribute("chkMarketing", chkMarketingValue);

        return "redirect:/auth/signup";
    }

    //회원 가입 페이지
    @GetMapping("/signup") // 중복되는 페이지
    public String showSignup(Model model) {
        return "signup2";
    }
}