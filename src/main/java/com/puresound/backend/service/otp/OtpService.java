package com.puresound.backend.service.otp;

import com.puresound.backend.dto.otp.VerifyOtpEmailRequest;

public interface OtpService {
    String generateCommonOtp(String email);
    boolean verifyCommonOtp(VerifyOtpEmailRequest request);
}
