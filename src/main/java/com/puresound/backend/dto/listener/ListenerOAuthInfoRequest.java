package com.puresound.backend.dto.listener;

import com.puresound.backend.constant.user.OAuth2Provider;

public record ListenerOAuthInfoRequest(
    String email,
    String firstname,
    String lastname,
    String avatar,
    OAuth2Provider oauth2
) {
}
