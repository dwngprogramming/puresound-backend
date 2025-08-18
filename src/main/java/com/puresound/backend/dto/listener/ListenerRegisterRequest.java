package com.puresound.backend.dto.listener;

import com.puresound.backend.constant.user.Gender;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record ListenerRegisterRequest(
        @NotBlank(message = "{USERNAME_NOT_BLANK}")
        @Pattern(regexp = "^[a-zA-Z0-9\\-_.]{6,30}$", message = "{USERNAME_PATTERN}")
        String username,

        @NotBlank(message = "{EMAIL_NOT_BLANK}")
        @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "{EMAIL_PATTERN}")
        String email,

        @NotBlank(message = "{PASSWORD_NOT_BLANK}")
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@\\-_=+.])[a-zA-Z\\d@\\-_=+.]{8,30}$", message = "{PASSWORD_PATTERN}")
        String password,

        @NotBlank(message = "{RETYPE_PASSWORD_NOT_BLANK}")
        String retypePassword,

        @NotBlank(message = "{FIRSTNAME_NOT_BLANK}")
        String firstname,

        @NotBlank(message = "{LASTNAME_NOT_BLANK}")
        String lastname,

        @NotNull(message = "{GENDER_REQUIRED}")
        Gender gender,

        @NotNull(message = "{DOB_REQUIRED}")
        @PastOrPresent(message = "{DOB_PAST_OR_PRESENT}")
        LocalDate dob
) {
    public boolean isValidRetypePassword() {
        return password.equals(retypePassword);
    }
}
