package com.puresound.backend.service.weather;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.puresound.backend.dto.location.LocationResponse;
import com.puresound.backend.dto.weather.WeatherResponse;
import com.puresound.backend.util.NormalizeLetterUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
@Service
public class DefaultCacheWeatherService implements CacheWeatherService {
    RedisTemplate<String, Object> redisTemplate;
    String WEATHER_CACHE_PREFIX = "weather:";
    Duration CACHE_TTL = Duration.ofMinutes(20);
    ObjectMapper objectMapper;

    @Override
    public WeatherResponse getWeather(LocationResponse location) {
        String locationKey = createLocationKey(location);
        String redisKey = WEATHER_CACHE_PREFIX + locationKey;

        try {
            Object cached = redisTemplate.opsForValue().get(redisKey);
            return cached != null ? objectMapper.convertValue(cached, WeatherResponse.class) : null;
        } catch (Exception e) {
            log.info("Not contains this weather from Redis cache: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public void saveWeather(WeatherResponse weatherResponse) {
        String locationKey = createLocationKey(weatherResponse.location());
        String redisKey = WEATHER_CACHE_PREFIX + locationKey;

        try {
            redisTemplate.opsForValue().set(redisKey, weatherResponse, CACHE_TTL);
        } catch (Exception e) {
            log.error("Failed to save weather to Redis cache: {}", e.getMessage());
        }
    }

    private String createLocationKey(LocationResponse location) {
        return String.format("%s_%s_%s",
                NormalizeLetterUtil.normalizeVietnamese(location.district()),
                NormalizeLetterUtil.normalizeVietnamese(location.province()),
                location.countryCode().toLowerCase());
    }
}

