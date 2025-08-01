package com.puresound.backend.security.jwt;

import com.puresound.backend.constant.user.UserType;

import java.util.List;

public record UserPrincipal(
        String id,
        String fullname,
        UserType userType,
        List<String> authorities
) {
}
