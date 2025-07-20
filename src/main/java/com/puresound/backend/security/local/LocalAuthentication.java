package com.puresound.backend.security.local;

import com.puresound.backend.constant.user.UserType;

import java.util.List;

public record LocalAuthentication(
        String id,
        String usernameOrEmail,
        String password,
        UserType userType,
        List<String> roles
) {}
