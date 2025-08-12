package com.puresound.backend.service.email;

import jakarta.mail.MessagingException;

public interface EmailService {
    void sendOtp(String to, String otp, long expMins) throws MessagingException;
}
