package com.puresound.backend.constant.api;

import lombok.experimental.UtilityClass;

@UtilityClass
public class BypassSecurity {
    public static final String[] PUBLIC_ENDPOINTS = {
            "/api/v1/auth/**",
            "/api/v1/token/**",
            "/api/v1/weather/current",
            "/api/v1/plans",
            "/api/v1/stream/**",
            "/api/v1/tracks/**",
            "/api/v1/artists/**",
            "/oauth2/**",
            "/api/v1/public/**",
            "/api/v1/files/public/**",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/documentation/**",
            "/actuator/health",
            "/favicon.ico",
            "/error"
    };
}
