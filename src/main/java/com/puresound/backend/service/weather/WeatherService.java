package com.puresound.backend.service.weather;

import com.puresound.backend.dto.location.ReverseGeocodingRequest;
import com.puresound.backend.dto.weather.WeatherResponse;

public interface WeatherService {
    WeatherResponse getCurrentWeather(ReverseGeocodingRequest request);
}
