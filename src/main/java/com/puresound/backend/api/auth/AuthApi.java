package com.puresound.backend.api.auth;

import com.puresound.backend.constant.api.ApiMessage;
import com.puresound.backend.constant.api.LogLevel;
import com.puresound.backend.constant.user.UserType;
import com.puresound.backend.dto.ApiResponse;
import com.puresound.backend.dto.auth.LocalLoginRequest;
import com.puresound.backend.dto.auth.TokenResponse;
import com.puresound.backend.dto.listener.ListenerRegisterRequest;
import com.puresound.backend.exception.exts.BadRequestException;
import com.puresound.backend.security.cookie.CookieService;
import com.puresound.backend.security.jwt.JwtTokenProvider;
import com.puresound.backend.security.jwt.UserPrincipal;
import com.puresound.backend.security.local.LocalAuthenticationToken;
import com.puresound.backend.service.user.UserServiceImpl;
import com.puresound.backend.service.user.listener.ListenerService;
import com.puresound.backend.util.ApiResponseFactory;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/auth")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Authentication API", description = "API for Authentication feature")
@Slf4j
public class AuthApi {
    AuthenticationManager authenticationManager;
    JwtTokenProvider jwtTokenProvider;
    ApiResponseFactory apiResponseFactory;
    UserServiceImpl userService;
    ListenerService listenerService;
    CookieService cookieService;

    @PostMapping("/local/login")
    public ResponseEntity<ApiResponse<TokenResponse>> listenerLogin(@Valid @RequestBody LocalLoginRequest request,
                                                                    Locale locale,
                                                                    HttpServletResponse response) {
        if (!userService.isEmail(request.usernameOrEmail()) && !userService.isUsername(request.usernameOrEmail())) {
            throw new BadRequestException(ApiMessage.INVALID_UOE_FORMAT, LogLevel.INFO);
        }

        Authentication authentication = authenticationManager.authenticate(
                new LocalAuthenticationToken(request.usernameOrEmail(), request.password(), UserType.LISTENER)
        );

        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        String accessToken = jwtTokenProvider.generateAccessToken(principal);
        String refreshToken = jwtTokenProvider.generateRefreshToken(principal);

        TokenResponse tokenResponse = new TokenResponse(accessToken);
        long maxAgeSeconds = jwtTokenProvider.getExpRtMin() * 60;

        // Set RT to cookie
        cookieService.setCookie("refreshToken", refreshToken, maxAgeSeconds, response);

        return ResponseEntity.ok(apiResponseFactory.create(ApiMessage.LOGIN_SUCCESS, tokenResponse, locale));
    }

    @PostMapping("/local/artist/login")
    public ResponseEntity<ApiResponse<TokenResponse>> artistLogin() {
        return null;
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<Void>> listenerSignup(@Valid @RequestBody ListenerRegisterRequest request, Locale locale) {
        listenerService.register(request);
        return ResponseEntity.ok(apiResponseFactory.create(ApiMessage.SIGNUP_SUCCESS, locale));
    }

    @PostMapping("/signup/artist")
    public ResponseEntity<ApiResponse<Void>> artistSignup() {
        return null;
    }

    @PostMapping("/signup/staff")
    public ResponseEntity<ApiResponse<Void>> staffSignup() {
        return null;
    }

    @DeleteMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(Locale locale,
                                                    HttpServletResponse response) {
        // Set RT to cookie
        cookieService.deleteCookie("refreshToken", response);
        return ResponseEntity.ok(apiResponseFactory.create(ApiMessage.LOGOUT_SUCCESS, locale));
    }
}
