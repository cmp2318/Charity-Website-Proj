package com.ufund.api.ufundapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("Model-tier")
public class EmailRequestTest {

    private EmailRequest emailRequest;

    @BeforeEach
    public void setup(){
        emailRequest = new EmailRequest("nick123@gmail.com", "Body of email");
    }

    @Test
    public void testGetToEmail() {
        assertEquals("nick123@gmail.com", emailRequest.getToEmail());
    }

    @Test
    public void testGetBody(){
        assertEquals("Body of email", emailRequest.getBody());
    }

    
}
