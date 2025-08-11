package com.puresound.backend.service.user.listener;

import com.puresound.backend.constant.api.ApiMessage;
import com.puresound.backend.constant.api.LogLevel;
import com.puresound.backend.constant.user.OAuth2Type;
import com.puresound.backend.dto.auth.OAuth2ProviderRequest;
import com.puresound.backend.dto.auth.RefreshAuthentication;
import com.puresound.backend.dto.listener.ListenerOAuthInfoRequest;
import com.puresound.backend.dto.listener.ListenerRegisterRequest;
import com.puresound.backend.entity.user.listener.Listener;
import com.puresound.backend.exception.exts.BadRequestException;
import com.puresound.backend.mapper.listener.ListenerMapper;
import com.puresound.backend.repository.jpa.listener.ListenerRepository;
import com.puresound.backend.security.local.LocalAuthentication;
import com.puresound.backend.security.oauth2.OAuth2Authentication;
import com.puresound.backend.service.user.oauth2.OAuth2ProviderService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Service("LISTENER")
public class ListenerServiceImpl implements ListenerService {
    ListenerRepository listenerRepository;
    ListenerMapper listenerMapper;
    OAuth2ProviderService oAuth2ProviderService;

    @Override
    public LocalAuthentication findByUsernameOrEmail(String usernameOrEmail) {
        Listener listener = listenerRepository.findByUsernameOrEmail(usernameOrEmail)
                .orElseThrow(() -> new BadRequestException(ApiMessage.LISTENER_NOT_FOUND, LogLevel.INFO));
        return listenerMapper.toLocalAuthentication(listener);
    }

    @Override
    public RefreshAuthentication findToRefreshById(String id) {
        Listener listener = listenerRepository.findById(id)
                .orElseThrow(() -> new BadRequestException(ApiMessage.LISTENER_NOT_FOUND, LogLevel.INFO));
        return listenerMapper.toRefreshAuthentication(listener);
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
    public void register(ListenerRegisterRequest request) {
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
        listenerRepository.save(listener);
    }
}
