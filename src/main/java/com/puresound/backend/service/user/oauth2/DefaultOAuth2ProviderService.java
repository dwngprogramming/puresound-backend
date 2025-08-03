package com.puresound.backend.service.user.oauth2;

import com.puresound.backend.dto.auth.OAuth2ProviderRequest;
import com.puresound.backend.entity.user.oauth2.OAuth2Provider;
import com.puresound.backend.mapper.oauth2.OAuth2ProviderMapper;
import com.puresound.backend.repository.jpa.oauth2.OAuth2ProviderRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.Optional;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Service
public class DefaultOAuth2ProviderService implements OAuth2ProviderService {
    OAuth2ProviderRepository oauth2ProviderRepository;
    OAuth2ProviderMapper mapper;

    @Override
    public void save(OAuth2ProviderRequest request) {
        OAuth2Provider provider = mapper.createOAuth2Provider(request);
        oauth2ProviderRepository.save(provider);
    }

    @Override
    public Optional<OAuth2Provider> findByUserIdAndProvider(OAuth2ProviderRequest request) {
        return oauth2ProviderRepository.findByUserIdAndProvider(request.userId(), request.provider());
    }
}
