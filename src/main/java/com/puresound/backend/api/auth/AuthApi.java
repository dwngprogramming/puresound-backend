package com.puresound.backend.api.auth;

import com.puresound.backend.config.audit.UnauthenticatedAuditor;
import com.puresound.backend.constant.api.ApiMessage;
import com.puresound.backend.constant.api.LogLevel;
import com.puresound.backend.constant.user.UserType;
import com.puresound.backend.dto.ApiResponse;
import com.puresound.backend.dto.auth.*;
import com.puresound.backend.dto.listener.ListenerRegisterRequest;
import com.puresound.backend.dto.otp.SendOtpEmailRequest;
import com.puresound.backend.dto.otp.VerifyOtpEmailRequest;
import com.puresound.backend.exception.exts.BadRequestException;
import com.puresound.backend.security.cookie.CookieService;
import com.puresound.backend.security.jwt.JwtTokenProvider;
import com.puresound.backend.security.jwt.UserPrincipal;
import com.puresound.backend.security.local.LocalAuthenticationToken;
import com.puresound.backend.service.otp.OtpService;
import com.puresound.backend.service.subscription.listener.ListenerSubService;
import com.puresound.backend.service.user.CommonUserService;
import com.puresound.backend.service.user.listener.ListenerService;
import com.puresound.backend.service.user.token.BlacklistTokenService;
import com.puresound.backend.util.ApiResponseFactory;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/auth")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Tag(name = "Authentication API", description = "API for Authentication feature")
@Slf4j
public class AuthApi {
    final AuthenticationManager authenticationManager;
    final JwtTokenProvider jwtTokenProvider;
    final ApiResponseFactory apiResponseFactory;
    final CommonUserService commonUserService;
    final ListenerService listenerService;
    final ListenerSubService subService;
    final CookieService cookieService;
    final OtpService otpService;
    final BlacklistTokenService blacklistTokenService;

    @Value("${cookie.rt-name}")
    String rtKey;

    @Value("${cookie.stream-name}")
    String streamKey;

    @PostMapping("/local/login")
    public ResponseEntity<ApiResponse<TokenResponse>> listenerLogin(@Valid @RequestBody LocalLoginRequest request,
                                                                    Locale locale,
                                                                    HttpServletResponse response) {
        if (!commonUserService.isEmail(request.usernameOrEmail()) && !commonUserService.isUsername(request.usernameOrEmail())) {
            throw new BadRequestException(ApiMessage.INVALID_UOE_FORMAT, LogLevel.INFO);
        }

        Authentication authentication = authenticationManager.authenticate(
                new LocalAuthenticationToken(request.usernameOrEmail(), request.password(), UserType.LISTENER)
        );

        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        String accessToken = jwtTokenProvider.generateAccessToken(principal);
        String refreshToken = jwtTokenProvider.generateRefreshToken(principal);

        TokenResponse tokenResponse = new TokenResponse(accessToken);

        // Set RT to cookie
        cookieService.setCookie(rtKey, refreshToken, jwtTokenProvider.getExpRtMin(), response);

        // Create Stream Session Token for new login and set to cookie
        String streamToken = jwtTokenProvider.generateStreamToken(principal.id(), subService.isCurrentSubActive(principal.id()));
        cookieService.setCookie(streamKey, streamToken, jwtTokenProvider.getExpStreamMin(), response);

        return ResponseEntity.ok(apiResponseFactory.create(ApiMessage.LOGIN_SUCCESS, tokenResponse, locale));
    }

    @PostMapping("/local/artist/login")
    public ResponseEntity<ApiResponse<TokenResponse>> artistLogin() {
        return null;
    }

    @PostMapping("/signup")
    @UnauthenticatedAuditor(email = "#request.email()")
    public ResponseEntity<ApiResponse<Void>> listenerSignup(@Valid @RequestBody ListenerRegisterRequest request, Locale locale) throws MessagingException {
        listenerService.registerAndSendOtp(request);
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

    @PostMapping("/check-email")
    public ResponseEntity<ApiResponse<CheckExistsResponse>> checkEmailExists(@Valid @RequestBody CheckExistsRequest request, Locale locale) {
        boolean result = listenerService.isEmailExists(request.field());
        CheckExistsResponse checkExistsResponse = new CheckExistsResponse(result);
        if (result) {
            return ResponseEntity.ok(apiResponseFactory.create(ApiMessage.EMAIL_EXISTS, checkExistsResponse, locale));
        }
        return ResponseEntity.ok(apiResponseFactory.create(ApiMessage.NEW_EMAIL, checkExistsResponse, locale));
    }

    @PostMapping("/check-username")
    public ResponseEntity<ApiResponse<CheckExistsResponse>> checkUsernameExists(@Valid @RequestBody CheckExistsRequest request, Locale locale) {
        boolean result = listenerService.isUsernameExists(request.field());
        CheckExistsResponse checkExistsResponse = new CheckExistsResponse(result);
        if (result) {
            return ResponseEntity.ok(apiResponseFactory.create(ApiMessage.USERNAME_EXISTS, checkExistsResponse, locale));
        }
        return ResponseEntity.ok(apiResponseFactory.create(ApiMessage.NEW_USERNAME, checkExistsResponse, locale));
    }

    @PostMapping("/otp/send")
    public ResponseEntity<ApiResponse<Void>> sendOtp(@Valid @RequestBody SendOtpEmailRequest request, Locale locale) throws MessagingException {
        String email = request.email();
        listenerService.resendCommonOtp(email);
        return ResponseEntity.ok(apiResponseFactory.create(ApiMessage.OTP_SEND_SUCCESS, locale));
    }

    @PostMapping("/otp/verify")
    public ResponseEntity<ApiResponse<Void>> verifyOtp(@Valid @RequestBody VerifyOtpEmailRequest request, Locale locale) {
        boolean isValidOtp = otpService.verifyCommonOtp(request);
        if (!isValidOtp) {
            throw new BadRequestException(ApiMessage.OTP_INVALID, LogLevel.INFO);
        }
        listenerService.activateAccount(request.email());
        return ResponseEntity.ok(apiResponseFactory.create(ApiMessage.OTP_VERIFICATION_SUCCESS, locale));
    }

    @PatchMapping("/reset-password")
    @UnauthenticatedAuditor(email = "#request.email()")
    public ResponseEntity<ApiResponse<Void>> changePassword(@Valid @RequestBody ResetPasswordRequest request,
                                                            Locale locale) {
        listenerService.resetPassword(request);
        return ResponseEntity.ok(apiResponseFactory.create(ApiMessage.CHANGE_PASSWORD_SUCCESS, locale));
    }

    @DeleteMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(@CookieValue(value = "${cookie.rt-name}", required = false) String refreshToken,
                                                    HttpServletResponse response,
                                                    Locale locale) {
        // Check blacklist RT
        if (refreshToken != null && jwtTokenProvider.validateToken(refreshToken)) {
            Long remainingMillis = jwtTokenProvider.getRemainingExpiryMillis(refreshToken);
            blacklistTokenService.deactivateToken(refreshToken, remainingMillis);
        }

        // Set RT to cookie
        cookieService.deleteCookie(rtKey, response);
        return ResponseEntity.ok(apiResponseFactory.create(ApiMessage.LOGOUT_SUCCESS, locale));
    }
}
