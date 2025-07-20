package com.puresound.backend.security.local;

import com.puresound.backend.constant.user.UserType;

import java.util.List;

public record UserPrincipal(
        String id,
        UserType userType,
        List<String> roles
) {
}
