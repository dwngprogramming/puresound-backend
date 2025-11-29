package com.puresound.backend.mapper.oauth2;

import com.puresound.backend.dto.auth.OAuth2ProviderRequest;
import com.puresound.backend.entity.jpa.oauth2.OAuth2Provider;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@FieldDefaults(level = AccessLevel.PRIVATE)
public abstract class OAuth2ProviderDecorator implements OAuth2ProviderMapper {
    @Override
    public OAuth2Provider createOAuth2Provider(OAuth2ProviderRequest request) {
        return OAuth2Provider.builder()
                .userId(request.userId())
                .provider(request.provider())
                .linkedAt(LocalDateTime.now())
                .linked(true)
                .build();
    }
}
