package com.puresound.backend.api.auth;

import com.puresound.backend.constant.api.ApiMessage;
import com.puresound.backend.constant.api.LogLevel;
import com.puresound.backend.constant.user.UserType;
import com.puresound.backend.dto.ApiResponse;
import com.puresound.backend.dto.auth.AuthzCodeRequest;
import com.puresound.backend.dto.auth.CodeExchange;
import com.puresound.backend.dto.auth.RefreshAuthentication;
import com.puresound.backend.dto.auth.TokenResponse;
import com.puresound.backend.exception.exts.BadRequestException;
import com.puresound.backend.security.cookie.CookieService;
import com.puresound.backend.security.jwt.JwtTokenProvider;
import com.puresound.backend.security.jwt.UserPrincipal;
import com.puresound.backend.service.user.UserService;
import com.puresound.backend.service.user.router.UserServiceRouter;
import com.puresound.backend.service.user.token.TokenExchangeService;
import com.puresound.backend.util.ApiResponseFactory;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/token")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Token API", description = "API for Token handling feature")
@Slf4j
public class TokenHandlerApi {
    UserServiceRouter userServiceRouter;
    JwtTokenProvider jwtTokenProvider;
    ApiResponseFactory apiResponseFactory;
    CookieService cookieService;
    TokenExchangeService tokenExchangeService;

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<TokenResponse>> refreshToken(HttpServletRequest request, Locale locale) {
        String refreshToken = cookieService.getCookie("refreshToken", request);
        if (refreshToken == null) {
            throw new BadRequestException(ApiMessage.MISSING_REFRESH_TOKEN, LogLevel.DEBUG);
        }
        Jwt jwt = jwtTokenProvider.decodeToken(refreshToken);
        String userId = jwt.getSubject();
        String userType = jwt.getClaimAsString("userType");

        UserService userService = userServiceRouter.resolve(UserType.fromString(userType));
        RefreshAuthentication auth = userService.findToRefreshById(userId);
        UserPrincipal principal = new UserPrincipal(userId, auth.fullname(), UserType.fromString(userType), auth.roles());
        String accessToken = jwtTokenProvider.generateAccessToken(principal);

        TokenResponse tokenResponse = new TokenResponse(accessToken);
        return ResponseEntity.ok(apiResponseFactory.create(ApiMessage.LOGIN_SUCCESS, tokenResponse, locale));
    }

    @PostMapping("/exchange")
    public ResponseEntity<ApiResponse<TokenResponse>> exchangeToken(@RequestBody AuthzCodeRequest request, HttpServletResponse response, Locale locale) {
        String exchangeCode = request.code();
        Optional<CodeExchange> codeExchange = tokenExchangeService.consumeCode(exchangeCode);
        if (codeExchange.isEmpty()) {
            // Delete this login session (RT)
            cookieService.deleteCookie("refreshToken", response);
            throw new BadRequestException(ApiMessage.INVALID_EXCHANGE_CODE, LogLevel.WARN);
        }

        // Get id & type from code exchange
        CodeExchange exchange = codeExchange.get();
        String userId = exchange.userId();
        UserType userType = exchange.userType();

        UserService userService = userServiceRouter.resolve(userType);
        RefreshAuthentication auth = userService.findToRefreshById(userId);
        UserPrincipal principal = new UserPrincipal(userId, auth.fullname(), userType, auth.roles());
        String accessToken = jwtTokenProvider.generateAccessToken(principal);
        TokenResponse tokenResponse = new TokenResponse(accessToken);

        cookieService.deleteCookie("exchangeCode", response);
        return ResponseEntity.ok(apiResponseFactory.create(ApiMessage.LOGIN_SUCCESS, tokenResponse, locale));
    }
}
