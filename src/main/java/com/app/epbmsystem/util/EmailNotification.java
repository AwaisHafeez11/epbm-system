package com.app.epbmsystem.util;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailNotification {

    private final JavaMailSender javaMailSender;

    public EmailNotification(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendMail(String email, String message) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(email, email);
        msg.setSubject("Account Verification Token");
        msg.setText(message);
        javaMailSender.send(msg);
    }
}
