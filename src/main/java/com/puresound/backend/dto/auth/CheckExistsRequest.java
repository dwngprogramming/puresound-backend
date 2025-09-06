package com.puresound.backend.dto.auth;

import jakarta.validation.constraints.NotBlank;

public record CheckExistsRequest(
        @NotBlank(message = "{CHECK_FIELD_NOT_BLANK}")
        String field
) {
}
