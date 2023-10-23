package com.example.okmprice.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {
    private final JavaMailSender sender;
    private final String senderEmail ="rnfrnf301@gmail.com";


    public MailService(JavaMailSender sender) {
        this.sender = sender;
    }
    public MimeMessage createMail(String mail){
        MimeMessage message = sender.createMimeMessage();

        try {
            message.setFrom(senderEmail);
            message.setSubject("제목");
            String body = "";
            message.setText(body, "utf-8", "html");
            message.setRecipients(MimeMessage.RecipientType.TO,mail);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return message;
    }

    public int sendMail(String mail){
        MimeMessage message =  createMail(mail);
        sender.send(message);
        return 1;
    }




}
