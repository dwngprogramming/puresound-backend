package com.puresound.backend.dto.auth;

import com.puresound.backend.constant.user.UserType;

public record LoginRequest(
        String usernameOrEmail,
        String password,
        UserType userType
) {
}
