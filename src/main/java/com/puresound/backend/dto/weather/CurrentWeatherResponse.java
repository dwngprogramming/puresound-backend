package com.puresound.backend.dto.weather;

public record CurrentWeatherResponse(
        double tempC,
        double tempF,
        boolean isDay,
        String condition
) {
}
