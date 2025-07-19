package com.puresound.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisKeyValueAdapter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

// Thêm EnableKeyspaceEvents.ON_STARTUP để có cơ chế Phantom (Tự động xóa các index liên quan của 1 key-value)
@Configuration
@EnableRedisRepositories(
        enableKeyspaceEvents = RedisKeyValueAdapter.EnableKeyspaceEvents.ON_STARTUP
)
public class RedisConfig {
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // Use StringRedisSerializer to serialize key
        template.setKeySerializer(new StringRedisSerializer());

        // Use Jackson2JsonRedisSerializer to serialize value
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());

        // Configure Hash operation
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());

        template.afterPropertiesSet();
        return template;
    }

    @Bean
    public RedisTemplate<String, Long> redisTemplateLong(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Long> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // Use StringRedisSerializer to serialize key
        template.setKeySerializer(new StringRedisSerializer());

        // Use Jackson2JsonRedisSerializer to serialize value
        template.setValueSerializer(new Jackson2JsonRedisSerializer<>(Long.class));

        // Configure Hash operation
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(Long.class));

        template.afterPropertiesSet();
        return template;
    }
}
