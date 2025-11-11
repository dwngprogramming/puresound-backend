package com.puresound.backend.service.user.token;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Service
public class DefaultBlacklistTokenService implements BlacklistTokenService {
    // Todo: Migrate to save JTI instead of full token in future to security & avoid leak client information
    RedisTemplate<String, Object> redisTemplate;
    static String BLACKLIST_TOKEN_PREFIX = "blacklist:";

    @Override
    public void deactivateToken(String token, Long expMins) {
        String key = BLACKLIST_TOKEN_PREFIX + token;
        redisTemplate.opsForValue().set(key, "", expMins);
    }

    @Override
    public boolean isBlacklisted(String token) {
        String key = BLACKLIST_TOKEN_PREFIX + token;
        return redisTemplate.hasKey(key);
    }
}
