package com.puresound.backend.security.oauth2;

import com.puresound.backend.constant.user.UserType;

public record OAuth2Authentication(
        String id,
        String email,
        String fullname,
        UserType userType
) {
}
