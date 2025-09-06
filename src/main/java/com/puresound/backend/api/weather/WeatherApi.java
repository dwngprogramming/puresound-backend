package com.puresound.backend.api.weather;

import com.puresound.backend.constant.api.ApiMessage;
import com.puresound.backend.dto.ApiResponse;
import com.puresound.backend.dto.location.ReverseGeocodingRequest;
import com.puresound.backend.dto.weather.WeatherResponse;
import com.puresound.backend.service.weather.WeatherService;
import com.puresound.backend.util.ApiResponseFactory;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/weather")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Weather API", description = "API for Weather (Powered by WeatherAPI) feature")
@Slf4j
public class WeatherApi {
    ApiResponseFactory apiResponseFactory;
    WeatherService weatherService;

    @GetMapping("/current")
    public ResponseEntity<ApiResponse<WeatherResponse>> getCurrentWeather(
            @ModelAttribute @Valid ReverseGeocodingRequest request,
            Locale locale) {

        WeatherResponse weatherResponse = weatherService.getCurrentWeather(request);
        ApiResponse<WeatherResponse> apiResponse =
                apiResponseFactory.create(ApiMessage.GET_WEATHER_SUCCESS, weatherResponse, locale);
        return ResponseEntity.ok(apiResponse);
    }
}
