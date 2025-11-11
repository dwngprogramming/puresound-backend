package com.puresound.backend.service.cache;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Service
public class DefaultListenerCollectionService implements ListenerCollectionService {
    final RedisTemplate<String, Object> redisTemplate;
}
