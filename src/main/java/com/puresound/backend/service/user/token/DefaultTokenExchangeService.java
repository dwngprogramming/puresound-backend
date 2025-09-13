package com.puresound.backend.service.user.token;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.puresound.backend.dto.auth.CodeExchange;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;

@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Service
public class DefaultTokenExchangeService implements TokenExchangeService {
    static final String EXCHANGE_PREFIX = "exchange:";
    static final Duration EXPIRE_TIME = Duration.ofMinutes(2);
    final RedisTemplate<String, Object> redisTemplate;
    final ObjectMapper objectMapper;

    @Override
    public void storeCode(String code, CodeExchange request) {
        String key = EXCHANGE_PREFIX + code;
        redisTemplate.opsForValue().set(key, request, EXPIRE_TIME);
    }

    @Override
    public Optional<CodeExchange> consumeCode(String code) {
        String key = EXCHANGE_PREFIX + code;
        if (redisTemplate.hasKey(key)) {
            Object value = redisTemplate.opsForValue().get(key);
            if (value == null) return Optional.empty();

            CodeExchange response = objectMapper.convertValue(value, CodeExchange.class);
            redisTemplate.delete(key);
            return Optional.of(response);
        }
        return Optional.empty();
    }
}
