package com.dog.shop.web.email;

import com.dog.shop.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.Random;

@RestController
public class EmailController {

    @Autowired
    private EmailService emailService;

    // 인증 보내기
    @GetMapping("/send/email")
    public ResponseEntity<String> findPassword(@RequestParam String email, HttpSession session) throws MessagingException, UnsupportedEncodingException {
        Random random = new Random();
        String verificationCode = String.format("%06d", random.nextInt(1000000));

        // 인증 코드와 현재 시간을 세션에 저장합니다.
        session.setAttribute("verificationCode", verificationCode);
        session.setAttribute("codeCreationTime", LocalDateTime.now());

        emailService.sendMail(email, verificationCode);

        return ResponseEntity.ok("success");
    }

    @PostMapping("/send/email/bypassword")
    public ResponseEntity<String> findPassword2(@RequestBody EmailRequest emailRequest) throws MessagingException, UnsupportedEncodingException {
        String email = emailRequest.getEmail();
        Random random = new Random();
        String verificationCode = String.format("%06d", random.nextInt(1000000));

        // 인증 코드와 현재 시간을 세션에 저장합니다.

        emailService.sendMailPasswordChange(email, verificationCode);

        return ResponseEntity.ok("success");
    }
}
