package com.puresound.backend.service.user.oauth2;

import com.puresound.backend.dto.auth.OAuth2ProviderRequest;
import com.puresound.backend.entity.user.oauth2.OAuth2Provider;

import java.util.Optional;

public interface OAuth2ProviderService {
    void save(OAuth2ProviderRequest request);

    Optional<OAuth2Provider> findByUserIdAndProvider(OAuth2ProviderRequest request);
}
