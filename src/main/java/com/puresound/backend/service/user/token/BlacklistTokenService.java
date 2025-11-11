package com.puresound.backend.service.user.token;

public interface BlacklistTokenService {
    void deactivateToken(String token, Long ttlMillis);

    boolean isBlacklisted(String token);
}
