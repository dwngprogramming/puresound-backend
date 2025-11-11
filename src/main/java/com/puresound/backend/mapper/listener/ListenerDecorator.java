package com.puresound.backend.mapper.listener;

import com.puresound.backend.constant.user.UserType;
import com.puresound.backend.dto.auth.RefreshAuthentication;
import com.puresound.backend.dto.listener.ListenerOAuthInfoRequest;
import com.puresound.backend.entity.jpa.listener.Listener;
import com.puresound.backend.security.local.LocalAuthentication;
import com.puresound.backend.security.oauth2.OAuth2Authentication;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
public abstract class ListenerDecorator implements ListenerMapper {

    @Override
    public LocalAuthentication toLocalAuthentication(Listener listener) {
        String fullname = listener.getFirstname() + " " + listener.getLastname();
        return new LocalAuthentication(listener.getId(), listener.getUsername(), listener.getPassword(), fullname, UserType.LISTENER, List.of());
    }

    @Override
    public OAuth2Authentication toOAuth2Authentication(Listener listener) {
        String fullname = listener.getFirstname() + " " + listener.getLastname();
        return new OAuth2Authentication(listener.getId(), listener.getEmail(), fullname, UserType.LISTENER);
    }

    @Override
    public Listener toListener(ListenerOAuthInfoRequest request) {
        return Listener.builder()
                .email(request.email())
                .firstname(request.firstname())
                .lastname(request.lastname())
                .avatar(request.avatar())
                .build();
    }

    @Override
    public RefreshAuthentication toRefreshAuthentication(Listener listener) {
        String fullname = listener.getFirstname() + " " + listener.getLastname();
        return new RefreshAuthentication(listener.getId(), fullname, UserType.LISTENER, List.of());
    }
}
