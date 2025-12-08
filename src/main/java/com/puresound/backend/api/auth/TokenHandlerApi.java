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
import com.puresound.backend.exception.exts.UnauthorizedException;
import com.puresound.backend.security.cookie.CookieService;
import com.puresound.backend.security.jwt.JwtTokenProvider;
import com.puresound.backend.security.jwt.UserPrincipal;
import com.puresound.backend.service.subscription.listener.ListenerSubService;
import com.puresound.backend.service.user.UserService;
import com.puresound.backend.service.user.router.UserServiceRouter;
import com.puresound.backend.service.user.token.BlacklistTokenService;
import com.puresound.backend.service.user.token.TokenExchangeService;
import com.puresound.backend.util.ApiResponseFactory;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

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
    BlacklistTokenService blacklistTokenService;
    ListenerSubService subService;

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<TokenResponse>> refreshToken(@CookieValue(value = "refreshToken", required = false) String refreshToken,
                                                                   Locale locale) {
        if (refreshToken == null) {
            throw new BadRequestException(ApiMessage.MISSING_REFRESH_TOKEN, LogLevel.INFO);
        }

        // Check blacklist for RT
        if (blacklistTokenService.isBlacklisted(refreshToken)) {
            throw new UnauthorizedException(ApiMessage.INVALID_TOKEN, LogLevel.INFO);
        }

        Jwt jwt = jwtTokenProvider.decodeToken(refreshToken);
        String userId = jwt.getSubject();
        String userType = jwt.getClaimAsString("userType");

        UserService userService = userServiceRouter.resolve(UserType.fromString(userType));
        RefreshAuthentication auth = userService.findToRefreshById(userId);
        String email = userService.findEmailById(userId);
        UserPrincipal principal = new UserPrincipal(userId, email, auth.fullname(), UserType.fromString(userType), auth.roles());
        String accessToken = jwtTokenProvider.generateAccessToken(principal);

        TokenResponse tokenResponse = new TokenResponse(accessToken);
        return ResponseEntity.ok(apiResponseFactory.create(ApiMessage.LOGIN_SUCCESS, tokenResponse, locale));
    }

    @PostMapping("/exchange")
    public ResponseEntity<ApiResponse<TokenResponse>> exchangeToken(@RequestBody AuthzCodeRequest request,
                                                                    @CookieValue(value = "refreshToken", required = false) String refreshToken,
                                                                    HttpServletResponse response,
                                                                    Locale locale) {
        String exchangeCode = request.code();
        Optional<CodeExchange> codeExchange = tokenExchangeService.consumeCode(exchangeCode);
        if (codeExchange.isEmpty()) {
            // Delete this login session (RT) and blacklist RT if invalid code
            if (refreshToken != null) {
                Long remainingMillis = jwtTokenProvider.getRemainingExpiryMillis(refreshToken);
                blacklistTokenService.deactivateToken(refreshToken, remainingMillis);
            }
            cookieService.deleteCookie("refreshToken", response);
            throw new BadRequestException(ApiMessage.INVALID_EXCHANGE_CODE, LogLevel.WARN);
        }

        // Get id & type from code exchange
        CodeExchange exchange = codeExchange.get();
        String userId = exchange.userId();
        UserType userType = exchange.userType();

        UserService userService = userServiceRouter.resolve(userType);
        RefreshAuthentication auth = userService.findToRefreshById(userId);
        String email = userService.findEmailById(userId);
        UserPrincipal principal = new UserPrincipal(userId, email, auth.fullname(), userType, auth.roles());
        String accessToken = jwtTokenProvider.generateAccessToken(principal);
        TokenResponse tokenResponse = new TokenResponse(accessToken);

        cookieService.deleteCookie("exchangeCode", response);

        // Set stream session cookie for this user when exchange code success
        String streamToken = jwtTokenProvider.generateStreamToken(principal.id(), subService.isCurrentSubActive(principal.id()));
        cookieService.setCookie("stream_session", streamToken, jwtTokenProvider.getExpStreamMin(), response);

        return ResponseEntity.ok(apiResponseFactory.create(ApiMessage.LOGIN_SUCCESS, tokenResponse, locale));
    }
}
