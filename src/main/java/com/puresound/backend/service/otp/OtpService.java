package com.puresound.backend.service.otp;

import com.puresound.backend.dto.otp.OtpEmailRequest;

public interface OtpService {
    String generateSignUpOtp(String email);
    boolean verifySignUpOtp(OtpEmailRequest request);
}
