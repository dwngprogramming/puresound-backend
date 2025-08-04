package com.puresound.backend.dto.listener;

import com.puresound.backend.constant.user.OAuth2Type;

public record ListenerOAuthInfoRequest(
    String email,
    String firstname,
    String lastname,
    String avatar,
    OAuth2Type oauth2
) {
}
