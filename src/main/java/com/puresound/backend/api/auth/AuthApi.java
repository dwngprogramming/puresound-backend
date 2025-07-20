package com.puresound.backend.api.auth;

import com.puresound.backend.dto.ApiResponse;
import com.puresound.backend.dto.auth.TokenResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/auth")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Tag(name = "Authentication API", description = "API for Authentication feature")
@Slf4j
public class AuthApi {

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<TokenResponse>> listenerLogin() {
        return null;
    }

    @PostMapping("/artist/login")
    public ResponseEntity<ApiResponse<TokenResponse>> artistLogin() {
        return null;
    }

    @PostMapping("/staff/login")
    public ResponseEntity<ApiResponse<TokenResponse>> staffLogin() {
        return null;
    }
}
