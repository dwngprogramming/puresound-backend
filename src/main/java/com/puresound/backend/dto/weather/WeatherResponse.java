package com.puresound.backend.dto.weather;

import com.puresound.backend.dto.location.LocationResponse;

public record WeatherResponse(
        LocationResponse location,
        CurrentWeatherResponse current
) {
}
