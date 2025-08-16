package com.puresound.backend.service.otp;

import com.puresound.backend.dto.otp.VerifyOtpEmailRequest;

public interface OtpService {
    String generateSignUpOtp(String email);
    boolean verifySignUpOtp(VerifyOtpEmailRequest request);
}
