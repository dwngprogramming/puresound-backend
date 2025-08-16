package com.puresound.backend.dto.otp;

import jakarta.validation.constraints.NotBlank;

public record SendOtpEmailRequest(
        @NotBlank(message = "{EMAIL_NOT_BLANK}")
        String email
) {
}
