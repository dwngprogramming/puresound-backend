package com.puresound.backend.service.user.listener;

import com.puresound.backend.constant.api.ApiMessage;
import com.puresound.backend.constant.api.LogLevel;
import com.puresound.backend.dto.listener.ListenerOAuthInfoRequest;
import com.puresound.backend.entity.user.listener.Listener;
import com.puresound.backend.exception.exts.BadRequestException;
import com.puresound.backend.mapper.listener.ListenerMapper;
import com.puresound.backend.repository.listener.ListenerRepository;
import com.puresound.backend.security.local.LocalAuthentication;
import com.puresound.backend.security.oauth2.OAuth2Authentication;
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

    @Override
    public LocalAuthentication findByUsernameOrEmail(String usernameOrEmail) {
        Listener listener = listenerRepository.findByUsernameOrEmail(usernameOrEmail)
                .orElseThrow(() -> new BadRequestException(ApiMessage.LISTENER_NOT_FOUND, LogLevel.INFO));
        return listenerMapper.toLocalAuthentication(listener);
    }

    @Override
    public LocalAuthentication findById(String id) {
        Listener listener = listenerRepository.findById(id)
                .orElseThrow(() -> new BadRequestException(ApiMessage.LISTENER_NOT_FOUND, LogLevel.INFO));
        return listenerMapper.toLocalAuthentication(listener);
    }

    @Override
    public OAuth2Authentication findOAuth2ById(String id) {
        Listener listener = listenerRepository.findById(id)
                .orElseThrow(() -> new BadRequestException(ApiMessage.LISTENER_NOT_FOUND, LogLevel.INFO));
        return listenerMapper.toOAuth2Authentication(listener);
    }

    @Override
    public void save(ListenerOAuthInfoRequest request) {

    }

    @Override
    public boolean isEmailExists(String email) {
        return listenerRepository.existsByEmail(email);
    }
}
