package com.puresound.backend.service.user.listener;

import com.puresound.backend.constant.api.ApiMessage;
import com.puresound.backend.entity.user.listener.Listener;
import com.puresound.backend.exception.exts.BadRequestException;
import com.puresound.backend.mapper.listener.ListenerMapper;
import com.puresound.backend.repository.listener.ListenerRepository;
import com.puresound.backend.security.local.LocalAuthentication;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Service
@Qualifier("LISTENER")
public class ListenerServiceImpl implements ListenerService {
    ListenerRepository listenerRepository;
    ListenerMapper listenerMapper;

    @Override
    public LocalAuthentication findByUsernameOrEmail(String usernameOrEmail) {
        Listener listener = listenerRepository.findByUsernameOrEmail(usernameOrEmail)
                .orElseThrow(() -> new BadRequestException(ApiMessage.LISTENER_NOT_FOUND));
        return listenerMapper.toLocalAuthentication(listener);
    }
}
