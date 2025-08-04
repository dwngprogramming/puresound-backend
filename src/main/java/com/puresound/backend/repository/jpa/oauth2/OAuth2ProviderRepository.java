package com.puresound.backend.repository.jpa.oauth2;

import com.puresound.backend.constant.user.OAuth2Type;
import com.puresound.backend.entity.user.oauth2.OAuth2Provider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OAuth2ProviderRepository extends JpaRepository<OAuth2Provider, String> {
    Optional<OAuth2Provider> findByUserIdAndProvider(String userId, OAuth2Type provider);
}
