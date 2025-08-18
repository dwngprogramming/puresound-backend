package com.puresound.backend.security.jwt;

import com.puresound.backend.constant.user.UserType;

import java.util.List;

public record UserPrincipal(
        String id,
        String email,
        String fullname,
        UserType userType,
        List<String> authorities
) {
}
