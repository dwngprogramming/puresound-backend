package com.puresound.backend.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ResetPasswordRequest(
        @NotBlank(message = "{EMAIL_NOT_BLANK}")
        String email,

        @NotBlank(message = "{PASSWORD_NOT_BLANK}")
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@\\-_=+.])[a-zA-Z\\d@\\-_=+.]{8,30}$", message = "{PASSWORD_PATTERN}")
        String newPassword,

        @NotBlank(message = "{PASSWORD_NOT_BLANK}")
        String retypePassword
) {
    public boolean isValidRetypePassword() {
        return newPassword.equals(retypePassword);
    }
}
