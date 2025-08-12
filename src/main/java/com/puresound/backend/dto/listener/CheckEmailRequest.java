package com.puresound.backend.dto.listener;

import jakarta.validation.constraints.NotBlank;

public record CheckEmailRequest(
        @NotBlank(message = "{EMAIL_NOT_BLANK}")
        String email
) {
}
