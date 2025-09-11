package com.puresound.backend.dto.weather;

import com.puresound.backend.constant.weather.WeatherCondition;

public record CurrentWeatherResponse(
        double tempC,
        double tempF,
        boolean isDay,
        WeatherCondition condition
) {
}
