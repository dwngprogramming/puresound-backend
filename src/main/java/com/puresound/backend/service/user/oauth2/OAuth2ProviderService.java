package com.puresound.backend.service.user.oauth2;

import com.puresound.backend.dto.auth.OAuth2ProviderRequest;
import com.puresound.backend.entity.jpa.oauth2.OAuth2Provider;

import java.util.Optional;

public interface OAuth2ProviderService {
    void save(OAuth2ProviderRequest request);

    Optional<OAuth2Provider> findCurrentlyOAuth2Provider(OAuth2ProviderRequest request);

    boolean wasUnlinkedBefore(OAuth2ProviderRequest request);

    void link(OAuth2ProviderRequest request);
}
