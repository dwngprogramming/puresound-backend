package com.puresound.backend.service.weather;

import com.fasterxml.jackson.databind.JsonNode;
import com.puresound.backend.constant.api.ApiMessage;
import com.puresound.backend.constant.api.LogLevel;
import com.puresound.backend.constant.weather.WeatherCondition;
import com.puresound.backend.dto.location.LocationResponse;
import com.puresound.backend.dto.location.ReverseGeocodingRequest;
import com.puresound.backend.dto.weather.CurrentWeatherResponse;
import com.puresound.backend.dto.weather.WeatherResponse;
import com.puresound.backend.exception.InternalServerException;
import com.puresound.backend.exception.exts.NotFoundException;
import com.puresound.backend.service.location.LocationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.Locale;

@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Service
@Slf4j
public class DefaultWeatherService implements WeatherService {
    final LocationService locationService;
    final RestClient restClient;
    final RedisWeatherService redisWeatherService;

    @Value("${weatherApi.key}")
    String key;

    @Value("${weatherApi.url}")
    String url;

    @Override
    public WeatherResponse getCurrentWeather(ReverseGeocodingRequest request) {
        Locale locale = LocaleContextHolder.getLocale();

        // 1. Get location information
        LocationResponse locationResponse = locationService.reverseGeocode(request);

        // 2. Try to get from Redis cache first
        WeatherResponse cachedWeather = redisWeatherService.getWeather(locationResponse);
        if (cachedWeather != null) {
            return cachedWeather;
        }

        // 3. If not in cache, fetch from WeatherAPI using RestClient
        try {
            String requestUrl = UriComponentsBuilder.fromUriString(url)
                    .queryParam("key", key)
                    .queryParam("q", request.latitude() + "," + request.longitude())
                    .queryParam("lang", locale.getLanguage())
                    .toUriString();

            JsonNode jsonNode = restClient.get()
                    .uri(requestUrl)
                    .retrieve()
                    .body(JsonNode.class);

            WeatherResponse weatherResponse = extractWeather(jsonNode, locationResponse);

            // 4. Save to Redis cache (fire and forget)
            redisWeatherService.saveWeather(weatherResponse);

            return weatherResponse;

        } catch (RestClientException e) {
            log.error("Failed to fetch weather from API for location: {}", locationResponse, e);
            throw new InternalServerException("Failed to fetch current weather");
        } catch (Exception e) {
            log.error("Unexpected error while fetching weather", e);
            throw new NotFoundException(ApiMessage.WEATHER_NOT_FOUND, LogLevel.INFO);
        }
    }

    private WeatherResponse extractWeather(JsonNode root, LocationResponse location) {
        try {
            JsonNode current = root.path("current");
            double tempC = Math.round(current.path("temp_c").asDouble());
            double tempF = Math.round(current.path("temp_f").asDouble());
            String condition = current.path("condition").path("text").asText();
            boolean isDay = current.path("is_day").asInt() == 1;
            WeatherCondition mappingCondition = detectWeatherType(condition, !isDay);

            CurrentWeatherResponse currentWeather = new CurrentWeatherResponse(tempC, tempF, isDay, mappingCondition);
            return new WeatherResponse(location, currentWeather);
        } catch (Exception e) {
            log.error("Failed to parse weather data from API response", e);
            throw new InternalServerException("Failed to parse weather data");
        }
    }

    private WeatherCondition detectWeatherType(String condition, boolean isNight) {
        if (condition == null) return WeatherCondition.sunny;

        String lower = condition.toLowerCase();

        // Detect từ keywords của tất cả ngôn ngữ
        if (containsAny(lower, "tuyết", "snow", "blizzard", "snowy")) {
            return WeatherCondition.snowy;
        }

        if (containsAny(lower, "mưa", "rain", "storm", "rainy", "shower", "drizzle")) {
            return WeatherCondition.rainy;
        }

        if (!isNight && containsAny(lower, "nắng", "sunny", "sun", "bright")) {
            return WeatherCondition.sunny;
        }

        if (isNight && containsAny(lower, "clear", "trong", "fair")) {
            return WeatherCondition.clear_night;
        }

        return WeatherCondition.cloudy;
    }

    private boolean containsAny(String text, String... keywords) {
        return Arrays.stream(keywords).anyMatch(text::contains);
    }
}
