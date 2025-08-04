package com.puresound.backend.dto.auth;

import com.puresound.backend.constant.user.OAuth2Type;

public record OAuth2ProviderRequest(
        String userId,
        OAuth2Type provider
) {
}
