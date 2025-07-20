package com.puresound.backend.mapper.listener;

import com.puresound.backend.constant.user.UserType;
import com.puresound.backend.entity.user.listener.Listener;
import com.puresound.backend.security.local.LocalAuthentication;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
public abstract class ListenerMapperDecorator implements ListenerMapper {

    @Override
    public LocalAuthentication toLocalAuthentication(Listener listener) {
        return new LocalAuthentication(listener.getId(), listener.getUsername(), listener.getPassword(), UserType.LISTENER, List.of());
    }
}
