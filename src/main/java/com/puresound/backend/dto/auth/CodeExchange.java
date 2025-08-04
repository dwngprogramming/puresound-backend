package com.puresound.backend.dto.auth;

import com.puresound.backend.constant.user.UserType;

public record CodeExchange(
        String userId,
        UserType userType
) {
}
