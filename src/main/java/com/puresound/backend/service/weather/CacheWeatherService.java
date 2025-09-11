package com.puresound.backend.service.weather;

import com.puresound.backend.dto.location.LocationResponse;
import com.puresound.backend.dto.weather.WeatherResponse;

public interface CacheWeatherService {
    WeatherResponse getWeather(LocationResponse location);

    void saveWeather(WeatherResponse weatherResponse);
}
