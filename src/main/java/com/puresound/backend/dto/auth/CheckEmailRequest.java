package com.puresound.backend.dto.auth;

import jakarta.validation.constraints.NotBlank;

public record CheckEmailRequest(
        @NotBlank(message = "{EMAIL_NOT_BLANK}")
        String email
) {
}
