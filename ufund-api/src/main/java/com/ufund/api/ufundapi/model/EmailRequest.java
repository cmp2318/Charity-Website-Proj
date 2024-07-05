package com.ufund.api.ufundapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EmailRequest {
    
    @JsonProperty("toEmail") private String toEmail;

    @JsonProperty("body") private String body;

    /**
     * Creates a request email class to communicate over http requests
     * from front end to backend
     * @param toEmail email to send to
     * @param body the "receipt" of the email
     */
    public EmailRequest(@JsonProperty("toEmail") String toEmail, 
                        @JsonProperty("body") String body) {
        this.body = body;
        this.toEmail = toEmail; 
    }

    /**
     * Gets the email to send to
     * @return String representing email to send to
     */
    public String getToEmail() {
        return toEmail;
    }

    /**
     * Gets the body of the email to send
     * @return body of email
     */
    public String getBody(){
        return body;
    }


}
