package com.puresound.backend.api.auth;

import com.puresound.backend.constant.api.ApiMessage;
import com.puresound.backend.constant.api.LogLevel;
import com.puresound.backend.constant.user.UserType;
import com.puresound.backend.dto.ApiResponse;
import com.puresound.backend.dto.auth.LoginRequest;
import com.puresound.backend.dto.auth.TokenResponse;
import com.puresound.backend.exception.exts.BadRequestException;
import com.puresound.backend.security.jwt.JwtTokenProvider;
import com.puresound.backend.security.local.LocalAuthentication;
import com.puresound.backend.security.local.LocalAuthenticationToken;
import com.puresound.backend.security.local.UserPrincipal;
import com.puresound.backend.service.user.UserService;
import com.puresound.backend.service.user.UserServiceImpl;
import com.puresound.backend.service.user.router.UserServiceRouter;
import com.puresound.backend.util.ApiResponseFactory;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    UserServiceRouter userServiceRouter;

    @PostMapping("/local/login")
    public ResponseEntity<ApiResponse<TokenResponse>> listenerLogin(@Valid @RequestBody LoginRequest request, Locale locale) {
        if (!userService.isEmail(request.usernameOrEmail()) && !userService.isUsername(request.usernameOrEmail())) {
            throw new BadRequestException(ApiMessage.INVALID_UOE_FORMAT, LogLevel.INFO);
        }

        Authentication authentication = authenticationManager.authenticate(
                new LocalAuthenticationToken(request.usernameOrEmail(), request.password(), UserType.LISTENER)
        );

        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        String accessToken = jwtTokenProvider.generateAccessToken(principal);
        String refreshToken = jwtTokenProvider.generateRefreshToken(principal);

        TokenResponse tokenResponse = new TokenResponse(accessToken, refreshToken);
        return ResponseEntity.ok(apiResponseFactory.create(ApiMessage.LOGIN_SUCCESS, tokenResponse, locale));
    }

    @PostMapping("/local/artist/login")
    public ResponseEntity<ApiResponse<TokenResponse>> artistLogin() {
        return null;
    }

    @PostMapping("/local/staff/login")
    public ResponseEntity<ApiResponse<TokenResponse>> staffLogin() {
        return null;
    }

    @PostMapping("/local/refresh-token")
    public ResponseEntity<ApiResponse<TokenResponse>> refreshToken(@Valid @RequestBody @NotNull String refreshToken, Locale locale) {
        Jwt jwt = jwtTokenProvider.decodeToken(refreshToken);
        String userId = jwt.getSubject();
        String userType = jwt.getClaimAsString("userType");

        UserService userService = userServiceRouter.resolve(UserType.fromString(userType));
        LocalAuthentication auth = userService.findById(userId);
        UserPrincipal principal = new UserPrincipal(userId, auth.fullname(), UserType.fromString(userType), auth.roles());
        String accessToken = jwtTokenProvider.generateAccessToken(principal);

        TokenResponse tokenResponse = new TokenResponse(accessToken, refreshToken);
        return ResponseEntity.ok(apiResponseFactory.create(ApiMessage.LOGIN_SUCCESS, tokenResponse, locale));
    }
}
