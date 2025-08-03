package com.puresound.backend.dto.auth;

import com.puresound.backend.constant.user.UserType;

import java.util.List;

public record RefreshAuthentication(
        String id,
        String fullname,
        UserType userType,
        List<String> roles
) {
}
