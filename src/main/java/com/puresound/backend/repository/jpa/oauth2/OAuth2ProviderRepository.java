package com.puresound.backend.repository.jpa.oauth2;

import com.puresound.backend.constant.user.OAuth2Type;
import com.puresound.backend.entity.user.oauth2.OAuth2Provider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface OAuth2ProviderRepository extends JpaRepository<OAuth2Provider, String> {
    Optional<OAuth2Provider> findByUserIdAndProviderAndLinkedIsTrue(String userId, OAuth2Type provider);

    Optional<OAuth2Provider> findByUserIdAndProviderAndLinkedIsFalse(String userId, OAuth2Type provider);

    @Modifying
    @Query("""
            UPDATE OAuth2Provider p
            SET p.linked = true
            WHERE p.userId = :userId AND p.provider = :provider
            """)
    void link(String userId, OAuth2Type provider);
}
