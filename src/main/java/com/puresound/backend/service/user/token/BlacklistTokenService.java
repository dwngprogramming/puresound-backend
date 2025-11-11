package com.puresound.backend.service.user.token;

public interface BlacklistTokenService {
    void deactivateToken(String token, Long expMins);
    boolean isBlacklisted(String token);
}
