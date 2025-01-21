package com.hta2405.unite.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    public String sendAuthCode(String toEmail) {
        String authCode = UUID.randomUUID().toString().toUpperCase().substring(0, 8);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("[Unite] 비밀번호 초기화 코드 안내");
        message.setText("비밀번호 변경 인증번호는 [ " + authCode + " ] 입니다.");
        mailSender.send(message);
        return authCode;
    }
}
