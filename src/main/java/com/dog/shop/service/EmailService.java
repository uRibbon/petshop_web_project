package com.dog.shop.service;

import java.io.UnsupportedEncodingException;


import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;
    private static final String ADMIN_ADDRESS = "juni303@naver.com";

    @Async
    public void sendMail(String recipient, String verificationCode) throws UnsupportedEncodingException, MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        message.addRecipients(Message.RecipientType.TO, recipient); // 받는 사람
        message.setSubject("[FIND BY DOG 인증번호]");
        String text = "안녕하세요, FIND BY DOG 회원님!\n\n"
                + "귀하의 FIND BY DOG 계정 인증을 위한 요청이 접수되었습니다.\n"
                + "아래의 인증번호를 사용하여 인증을 완료해 주세요.\n\n"
                + "인증번호: " + verificationCode + "\n\n"
                + "이 메일에 대해 알지 못하시거나 요청하지 않으셨다면, 귀하의 계정이 위험할 수 있으므로 즉시 저희에게 연락해 주십시오.\n\n"
                + "감사합니다.\n"
                + "FIND BY DOG 팀 드림.";
        message.setText(text, "utf-8");
        message.setFrom(new InternetAddress(ADMIN_ADDRESS, "FIND BY DOG")); // Optional Sender Name
        mailSender.send(message);
    }

    /*@Async
    public void sendMail(String recipient, String password) throws UnsupportedEncodingException, MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        message.addRecipients(Message.RecipientType.TO, recipient); // 받는 사람
        message.setSubject("[버블펫 비밀번호 변경]");
        String text = "안녕하세요, 버블펫 회원님!\n\n"
                + "귀하의 버블펫 계정의 비밀번호 변경 요청이 접수되었습니다.\n"
                + "아래의 새로운 비밀번호를 사용하여 로그인할 수 있습니다.\n\n"
                + "새로운 비밀번호: " + password + "\n\n"
                + "로그인 후, 보안을 위해 비밀번호를 다시 변경해 주세요.\n"
                + "이 메일에 대해 알지 못하시거나 요청하지 않으셨다면, 귀하의 계정이 위험할 수 있으므로 즉시 저희에게 연락해 주십시오.\n\n"
                + "감사합니다.\n"
                + "버블펫 팀 드림.";
        message.setText(text, "utf-8");
        message.setFrom(new InternetAddress(ADMIN_ADDRESS, "버블펫")); // Optional Sender Name
        mailSender.send(message);
    }*/
    
    

}