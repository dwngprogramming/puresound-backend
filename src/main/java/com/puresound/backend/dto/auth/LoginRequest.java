package com.puresound.backend.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record LoginRequest(
        @NotBlank(message = "{USERNAME_NOT_BLANK}")
        String usernameOrEmail,

        @NotBlank(message = "{PASSWORD_NOT_BLANK}")
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@\\-_=+.])[a-zA-Z\\d@\\-_=+.]{8,30}$", message = "{PASSWORD_PATTERN}")
        String password
) {
}
