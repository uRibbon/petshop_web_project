package com.dog.shop.web.email;

import com.dog.shop.domain.User;
import com.dog.shop.repository.UserRepository;
import com.dog.shop.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@RestController

public class EmailController {

    @Autowired
    private EmailService emailService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL_CHARS = "@$!%*?&";
    private static final String ALL_CHARS = LOWERCASE + UPPERCASE + DIGITS + SPECIAL_CHARS;

    private static final SecureRandom random = new SecureRandom();


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
    public  ResponseEntity<?> findPassword2(@RequestBody EmailRequest emailRequest, HttpSession session) throws MessagingException, UnsupportedEncodingException {
        String email = emailRequest.getEmail();
        // Random random = new Random();
        // String verificationCode = String.format("%06d", random.nextInt(1000000));

        // 클래스 이름을 사용하여 static 메서드를 호출
        String randomPassword = generateRandomPassword(10); // 여기서 10은 원하는 비밀번호의 길이입니다.



        // 비밀번호를 세션에 저장합니다.
        session.setAttribute("randomPassword", randomPassword);
        emailService.sendMailPasswordChange(email, randomPassword);



        // TODO 이메일 에러처리
        User user = userRepository.findByEmail(email).orElseThrow();
        String encodedPassword = passwordEncoder.encode(randomPassword);
        user.setPassword(encodedPassword);
        userRepository.save(user);
        return ResponseEntity.ok().body(Map.of("message", "success"));
    }

    public static String generateRandomPassword(int length) {
        if (length < 8) {
            throw new IllegalArgumentException("Password length should be at least 8 characters");
        }

        StringBuilder password = new StringBuilder(length);

        password.append(LOWERCASE.charAt(random.nextInt(LOWERCASE.length())));
        password.append(UPPERCASE.charAt(random.nextInt(UPPERCASE.length())));
        password.append(DIGITS.charAt(random.nextInt(DIGITS.length())));
        password.append(SPECIAL_CHARS.charAt(random.nextInt(SPECIAL_CHARS.length())));

        for (int i = 4; i < length; i++) {
            password.append(ALL_CHARS.charAt(random.nextInt(ALL_CHARS.length())));
        }

        return password.toString();
    }
}
