package com.ufund.api.ufundapi.viewmodel;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;

import static org.mockito.Mockito.verify;

public class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailService emailService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
    }

    @Test
    public void testSendEmail() {
        String toEmail = "test@example.com";
        String body = "This is a test email body";
        emailService.sendEmail(toEmail, body);
        // Verify that the send method of JavaMailSender is called with the correct arguments
        verify(mailSender, atLeastOnce()).send(any(SimpleMailMessage.class));
    }
}

