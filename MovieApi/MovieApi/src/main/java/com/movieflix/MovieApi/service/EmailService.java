package com.movieflix.MovieApi.service;


import com.movieflix.MovieApi.dto.MailBody;
import org.eclipse.angus.mail.imap.protocol.MailboxInfo;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    
    
    private final JavaMailSender javaMailSender;
    
    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }
    
    public void sendSimpleMessage(MailBody mailBody){
        SimpleMailMessage message =new SimpleMailMessage();
        message.setTo(mailBody.getTo());
        message.setFrom("");
        message.setSubject(mailBody.getSubject());
        message.setText(mailBody.getText());
        
        javaMailSender.send(message);
    }
    
}
