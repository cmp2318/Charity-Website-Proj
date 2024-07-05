package com.ufund.api.ufundapi.viewmodel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Service to send an email receipt to the user
 * Uses javaMailSender to send email
 */
@Service
public class EmailService {
    
    @Autowired
    private JavaMailSender mailSender;


    public boolean sendEmail(String toEmail, String body){

        SimpleMailMessage message = new SimpleMailMessage();

        //set sender
        message.setFrom("smilesunlimited585@gmail.com");

        //set receiver
        message.setTo(toEmail);

        //set subject
        message.setSubject("Smiles Unlimited Receipt");

        //set the body
        message.setText(body);

        //send the email
        try {
            mailSender.send(message);
            return true;
        } catch (MailException e) {
            return false;
        }
        
    }
}
