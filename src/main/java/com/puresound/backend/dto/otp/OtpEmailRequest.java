package com.puresound.backend.dto.otp;

import jakarta.validation.constraints.NotBlank;

public record OtpEmailRequest(
        @NotBlank(message = "{EMAIL_NOT_BLANK}")
        String email,
        @NotBlank(message = "{OTP_NOT_BLANK}")
        String otp
) {
}
