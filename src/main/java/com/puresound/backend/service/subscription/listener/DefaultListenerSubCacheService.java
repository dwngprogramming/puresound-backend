package com.puresound.backend.service.subscription.listener;

import com.puresound.backend.dto.subscription.listener.ListenerSubCache;
import com.puresound.backend.dto.subscription.listener.ListenerSubResponse;
import com.puresound.backend.exception.InternalServerException;
import com.puresound.backend.mapper.subscription.listener.ListenerSubCacheMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
@Service
public class DefaultListenerSubCacheService implements ListenerSubCacheService {
    RedisTemplate<String, Object> redisTemplate;
    ListenerSubCacheMapper mapper;
    String SUBSCRIPTION_PREFIX = "subscription:";

    @Override
    public ListenerSubCache get(String listenerId) {
        String cacheKey = SUBSCRIPTION_PREFIX + listenerId;
        try {
            Object cached = redisTemplate.opsForValue().get(cacheKey);
            if ((cached instanceof ListenerSubCache)) {
                return (ListenerSubCache) cached;
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void create(String listenerId, ListenerSubResponse subscription) {
        ListenerSubCache cache = mapper.toCache(subscription);
        String cacheKey = SUBSCRIPTION_PREFIX + listenerId;
        try {
            Instant currentTime = Instant.now();
            Duration ttl = Duration.between(currentTime, cache.endDate());
            redisTemplate.opsForValue().set(cacheKey, cache, ttl);
        } catch (Exception e) {
            throw new InternalServerException("Failed to save subscription to Redis cache: " + e.getMessage());
        }
    }
}
