package com.puresound.backend.security.oauth2;

import com.puresound.backend.constant.api.ApiMessage;
import com.puresound.backend.constant.api.LogLevel;
import com.puresound.backend.constant.user.OAuth2Provider;
import com.puresound.backend.constant.user.UserType;
import com.puresound.backend.dto.listener.ListenerOAuthInfoRequest;
import com.puresound.backend.exception.InternalServerException;
import com.puresound.backend.exception.exts.BadRequestException;
import com.puresound.backend.security.cookie.CookieService;
import com.puresound.backend.security.jwt.JwtTokenProvider;
import com.puresound.backend.security.jwt.UserPrincipal;
import com.puresound.backend.service.user.UserService;
import com.puresound.backend.service.user.listener.ListenerService;
import com.puresound.backend.service.user.router.UserServiceRouter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Component
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {
    final JwtTokenProvider tokenProvider;
    final UserServiceRouter userServiceRouter;
    final CookieService cookieService;

    @Value("${listener.callback-url}")
    String listenerCallbackUrl;

    @Value("${artist.callback-url}")
    String artistCallbackUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
        String registrationId = token.getAuthorizedClientRegistrationId();
        OAuth2Provider provider;
        switch (registrationId) {
            case "google" -> provider = OAuth2Provider.GOOGLE;
            case "facebook" -> provider = OAuth2Provider.FACEBOOK;
            default -> throw new InternalServerException(ApiMessage.INVALID_OAUTH2_PROVIDER.name());
        }

        OAuth2User user = token.getPrincipal();
        String email = user.getAttribute("email");
        String name = user.getAttribute("name");
        String givenName = user.getAttribute("given_name");
        String familyName = user.getAttribute("family_name");
        String picture = user.getAttribute("picture");

        String userType = request.getParameter("userType");
        String callbackUrl;

        // Verify user exists
        UserService userService = userServiceRouter.resolve(UserType.fromString(userType));
        switch (userType) {
            case "LISTENER" -> {
                ListenerService listenerService = (ListenerService) userService;
                if (listenerService.isEmailExists(email)) {
                    throw new BadRequestException(ApiMessage.EMAIL_EXISTS, LogLevel.INFO);
                }
                ListenerOAuthInfoRequest listenerRequest = new ListenerOAuthInfoRequest(email, givenName, familyName, picture, provider);
                listenerService.save(listenerRequest);
                callbackUrl = listenerCallbackUrl;
            }
            case "ARTIST", "STAFF" -> throw new BadRequestException(ApiMessage.FEATURE_IN_FUTURE, LogLevel.INFO);
            default -> throw new BadRequestException(ApiMessage.UNKNOWN_USER_TYPE, LogLevel.INFO);
        }

        // Generate tokens
        UserPrincipal principal = new UserPrincipal(email, name, UserType.fromString(userType), List.of());
        String accessToken = tokenProvider.generateAccessToken(principal);
        String refreshToken = tokenProvider.generateRefreshToken(principal);
        long maxAgeSeconds = tokenProvider.getExpAtMin() * 60;

        // Send tokens
        cookieService.setCookie("refreshToken", refreshToken, maxAgeSeconds, response);
        response.sendRedirect(callbackUrl + "?accessToken=" + accessToken);
    }
}
