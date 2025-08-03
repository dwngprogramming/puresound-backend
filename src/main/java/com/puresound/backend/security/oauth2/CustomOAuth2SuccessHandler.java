package com.puresound.backend.security.oauth2;

import com.github.f4b6a3.ulid.UlidCreator;
import com.puresound.backend.constant.api.ApiMessage;
import com.puresound.backend.constant.api.LogLevel;
import com.puresound.backend.constant.user.OAuth2Type;
import com.puresound.backend.constant.user.UserType;
import com.puresound.backend.dto.auth.CodeExchange;
import com.puresound.backend.dto.listener.ListenerOAuthInfoRequest;
import com.puresound.backend.exception.InternalServerException;
import com.puresound.backend.exception.exts.BadRequestException;
import com.puresound.backend.security.cookie.CookieService;
import com.puresound.backend.security.jwt.JwtTokenProvider;
import com.puresound.backend.security.jwt.UserPrincipal;
import com.puresound.backend.service.user.UserService;
import com.puresound.backend.service.user.listener.ListenerService;
import com.puresound.backend.service.user.router.UserServiceRouter;
import com.puresound.backend.service.user.token.TokenExchangeService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Locale;

@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Component
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {
    final JwtTokenProvider tokenProvider;
    final UserServiceRouter userServiceRouter;
    final CookieService cookieService;
    final TokenExchangeService tokenExchangeService;
    final MessageSource messageSource;

    @Value("${listener.callback-url}")
    String listenerCallbackUrl;

    @Value("${artist.callback-url}")
    String artistCallbackUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {
        Locale locale = LocaleContextHolder.getLocale();

        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
        String registrationId = token.getAuthorizedClientRegistrationId();
        OAuth2Type providerType;
        switch (registrationId) {
            case "google" -> providerType = OAuth2Type.GOOGLE;
            case "facebook" -> providerType = OAuth2Type.FACEBOOK;
            default -> throw new InternalServerException(ApiMessage.INVALID_OAUTH2_PROVIDER.name());
        }

        OAuth2User user = token.getPrincipal();
        String email = user.getAttribute("email");
        String givenName = user.getAttribute("given_name");
        String familyName = user.getAttribute("family_name");
        String picture = user.getAttribute("picture");


        String state = request.getParameter("state");
        if (state == null) {
            response.sendRedirect("/login");
            return;
        }

        String userType = new String(Base64.getUrlDecoder().decode(state));
        String callbackUrl;
        String userId;
        String fullname;
        boolean isNew = false;

        // Verify user exists
        UserService userService = userServiceRouter.resolve(UserType.fromString(userType.toUpperCase()));
        switch (userType) {
            case "listener" -> {
                ListenerService listenerService = (ListenerService) userService;
                if (!listenerService.isEmailExists(email)) {
                    // Save OAuth2 Provider inside Listener Service
                    ListenerOAuthInfoRequest listenerRequest = new ListenerOAuthInfoRequest(email, givenName, familyName, picture, providerType);
                    listenerService.save(listenerRequest);
                } else {
                    // Link OAuth2 Provider to Listener if email exists and not linked
                    if (!listenerService.isLinkedOAuth2Provider(email, providerType)) {
                        listenerService.linkOAuth2ToListener(email, providerType);
                        isNew = true;
                    }
                }
                OAuth2Authentication listener = listenerService.findOAuth2ByEmail(email);
                userId = listener.id();
                fullname = listener.fullname();
                callbackUrl = listenerCallbackUrl;
            }
            case "artist", "staff" -> throw new BadRequestException(ApiMessage.FEATURE_IN_FUTURE, LogLevel.INFO);
            default -> throw new BadRequestException(ApiMessage.UNKNOWN_USER_TYPE, LogLevel.INFO);
        }

        // Generate refresh token (Cookie) & Exchange code (Query param)
        UserPrincipal principal = new UserPrincipal(userId, fullname, UserType.fromString(userType), List.of());
        String refreshToken = tokenProvider.generateRefreshToken(principal);
        long maxAgeSeconds = tokenProvider.getExpRtMin() * 60;
        String exchangeCode = UlidCreator.getMonotonicUlid().toString();

        // Save exchange code to Redis with TTL = 2 minutes (define in service)
        CodeExchange codeExchange = new CodeExchange(userId, UserType.fromString(userType));
        tokenExchangeService.storeCode(exchangeCode, codeExchange);

        // Set RT to cookie, code to cookie with httpOnly = false
        cookieService.setCookie("refreshToken", refreshToken, maxAgeSeconds, response);
        cookieService.setCookieWithHttpOnlyFalse("exchangeCode", exchangeCode, maxAgeSeconds, response);

        // Set "linked to OAuth 2 provider if email exists in system" message to cookie
        String message = isNew ? messageSource.getMessage(ApiMessage.LINKED_TO_OAUTH2.name(), new Object[]{StringUtils.capitalize(providerType.name().toLowerCase())}, locale) : null;
        if (message != null) {
            // Session cookie, delete in frontend. Base64
            String encodedMessage = Base64.getEncoder().encodeToString(
                    message.getBytes(StandardCharsets.UTF_8)
            );
            cookieService.setCookieWithHttpOnlyFalse("linkedProvider", encodedMessage, -1, response);
        }
        String redirectUrl = UriComponentsBuilder
                .fromUriString(callbackUrl)
                .build()
                .toUriString();

        response.sendRedirect(redirectUrl);
    }
}
