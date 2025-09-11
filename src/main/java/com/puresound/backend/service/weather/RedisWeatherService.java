package com.puresound.backend.service.weather;

import com.puresound.backend.dto.location.LocationResponse;
import com.puresound.backend.dto.weather.WeatherResponse;

public interface RedisWeatherService {
    WeatherResponse getWeather(LocationResponse location);

    void saveWeather(WeatherResponse weatherResponse);
}
