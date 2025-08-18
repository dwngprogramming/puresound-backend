package com.puresound.backend.service.user.listener;

import com.puresound.backend.constant.Status;
import com.puresound.backend.constant.api.ApiMessage;
import com.puresound.backend.constant.api.LogLevel;
import com.puresound.backend.constant.user.OAuth2Type;
import com.puresound.backend.dto.auth.OAuth2ProviderRequest;
import com.puresound.backend.dto.auth.RefreshAuthentication;
import com.puresound.backend.dto.auth.ResetPasswordRequest;
import com.puresound.backend.dto.listener.ListenerOAuthInfoRequest;
import com.puresound.backend.dto.listener.ListenerRegisterRequest;
import com.puresound.backend.dto.listener.ListenerResponse;
import com.puresound.backend.entity.user.listener.Listener;
import com.puresound.backend.exception.exts.BadRequestException;
import com.puresound.backend.mapper.listener.ListenerMapper;
import com.puresound.backend.repository.jpa.listener.ListenerRepository;
import com.puresound.backend.security.local.LocalAuthentication;
import com.puresound.backend.security.oauth2.OAuth2Authentication;
import com.puresound.backend.service.email.EmailService;
import com.puresound.backend.service.otp.OtpService;
import com.puresound.backend.service.user.oauth2.OAuth2ProviderService;
import jakarta.mail.MessagingException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Service("LISTENER")
public class ListenerServiceImpl implements ListenerService {
    ListenerRepository listenerRepository;
    ListenerMapper listenerMapper;
    OAuth2ProviderService oAuth2ProviderService;
    OtpService otpService;
    EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public LocalAuthentication loginByUsernameOrEmail(String usernameOrEmail) {
        if (listenerRepository.isLockedAccount(usernameOrEmail)) {
            throw new BadRequestException(ApiMessage.LOCKED_ACCOUNT, LogLevel.WARN);
        }

        Listener listener = listenerRepository.loginByUsernameOrEmail(usernameOrEmail)
                .orElseThrow(() -> new BadRequestException(ApiMessage.LISTENER_NOT_FOUND, LogLevel.INFO));

        // Không bị locked thì trả về listener
        return listenerMapper.toLocalAuthentication(listener);
    }

    @Override
    public RefreshAuthentication findToRefreshById(String id) {
        Listener listener = listenerRepository.findById(id)
                .orElseThrow(() -> new BadRequestException(ApiMessage.LISTENER_NOT_FOUND, LogLevel.INFO));
        return listenerMapper.toRefreshAuthentication(listener);
    }

    @Override
    public void activateAccount(String email) {
        listenerRepository.findByEmail(email)
                .ifPresentOrElse(listener -> {
                    listener.setStatus(Status.ACTIVE);
                    listenerRepository.save(listener);
                }, () -> {
                    throw new BadRequestException(ApiMessage.LISTENER_NOT_FOUND, LogLevel.INFO);
                });
    }

    @Transactional
    @Override
    public void resetPassword(ResetPasswordRequest request) {
        if (!isEmailExists(request.email())) {
            throw new BadRequestException(ApiMessage.EMAIL_NOT_EXISTS, LogLevel.INFO);
        }

        if (!request.isValidRetypePassword()) {
            throw new BadRequestException(ApiMessage.RETYPE_PASSWORD_NOT_MATCH, LogLevel.INFO);
        }

        String encodedPassword = passwordEncoder.encode(request.newPassword());
        listenerRepository.resetPassword(request.email(), encodedPassword);
    }

    @Override
    public String findEmailById(String userId) {
        return listenerRepository.findById(userId)
                .map(Listener::getEmail)
                .orElseThrow(() -> new BadRequestException(ApiMessage.LISTENER_NOT_FOUND, LogLevel.INFO));
    }

    @Transactional
    @Override
    public void updateLastLogin(String id) {
        listenerRepository.updateLastLoginAt(id);
    }

    @Override
    public OAuth2Authentication findOAuth2ById(String id) {
        Listener listener = listenerRepository.findById(id)
                .orElseThrow(() -> new BadRequestException(ApiMessage.LISTENER_NOT_FOUND, LogLevel.INFO));
        return listenerMapper.toOAuth2Authentication(listener);
    }

    @Override
    public void save(ListenerOAuthInfoRequest request) {
        Listener listener = listenerMapper.toListener(request);
        String listenerId = listenerRepository.save(listener).getId();

        // Save OAuth2 Provider for this listener
        OAuth2Type provider = request.oauth2();
        OAuth2ProviderRequest oauth2ProviderRequest = new OAuth2ProviderRequest(listenerId, provider);
        oAuth2ProviderService.save(oauth2ProviderRequest);
    }

    @Override
    public boolean isEmailExists(String email) {
        return listenerRepository.existsByEmail(email);
    }

    @Override
    public boolean isUsernameExists(String username) {
        return listenerRepository.existsByUsername(username);
    }

    @Override
    public OAuth2Authentication findOAuth2ByEmail(String email) {
        Listener listener = listenerRepository.findByEmail(email)
                .orElseThrow(() -> new BadRequestException(ApiMessage.LISTENER_NOT_FOUND, LogLevel.INFO));
        return listenerMapper.toOAuth2Authentication(listener);
    }

    @Override
    public String findIdByEmail(String email) {
        return listenerRepository.findByEmail(email)
                .map(Listener::getId)
                .orElseThrow(() -> new BadRequestException(ApiMessage.LISTENER_NOT_FOUND, LogLevel.INFO));
    }

    @Override
    public boolean isLinkedOAuth2Provider(String email, OAuth2Type provider) {
        String listenerId = findIdByEmail(email);
        OAuth2ProviderRequest oauth2ProviderRequest = new OAuth2ProviderRequest(listenerId, provider);
        return oAuth2ProviderService
                .findCurrentlyOAuth2Provider(oauth2ProviderRequest)
                .isPresent();
    }

    @Override
    public void linkOAuth2ToListener(String email, OAuth2Type provider) {
        String listenerId = findIdByEmail(email);
        OAuth2ProviderRequest request = new OAuth2ProviderRequest(listenerId, provider);
        // If unlinked before (isLinked = false now), link again
        if (oAuth2ProviderService.wasUnlinkedBefore(request)) {
            oAuth2ProviderService.link(request);
        } else {
            oAuth2ProviderService.save(request);
        }
    }

    @Override
    public void registerAndSendOtp(ListenerRegisterRequest request) throws MessagingException {
        if (isEmailExists(request.email())) {
            throw new BadRequestException(ApiMessage.EMAIL_EXISTS, LogLevel.INFO);
        }

        if (isUsernameExists(request.username())) {
            throw new BadRequestException(ApiMessage.USERNAME_EXISTS, LogLevel.INFO);
        }

        if (!request.isValidRetypePassword()) {
            throw new BadRequestException(ApiMessage.RETYPE_PASSWORD_NOT_MATCH, LogLevel.INFO);
        }

        Listener listener = listenerMapper.toListener(request);
        // Set status to "LOCKED"
        listener.setStatus(Status.LOCKED);
        listener.setPassword(passwordEncoder.encode(request.password()));
        listenerRepository.save(listener);

        // Send OTP
        String otp = otpService.generateCommonOtp(request.email());
        emailService.sendOtp(request.email(), otp, 5);
    }

    @Override
    public ListenerResponse getById(String id) {
        Listener listener = listenerRepository.findById(id)
                .orElseThrow(() -> new BadRequestException(ApiMessage.LISTENER_NOT_FOUND, LogLevel.INFO));
        return listenerMapper.toListenerResponse(listener);
    }

    @Override
    public void resendCommonOtp(String email) throws MessagingException {
        String otp = otpService.generateCommonOtp(email);
        emailService.sendOtp(email, otp, 5);
    }
}
