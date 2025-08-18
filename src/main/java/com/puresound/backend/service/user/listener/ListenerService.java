package com.puresound.backend.service.user.listener;

import com.puresound.backend.constant.user.OAuth2Type;
import com.puresound.backend.dto.listener.ListenerOAuthInfoRequest;
import com.puresound.backend.dto.listener.ListenerRegisterRequest;
import com.puresound.backend.dto.listener.ListenerResponse;
import com.puresound.backend.security.oauth2.OAuth2Authentication;
import com.puresound.backend.service.user.UserService;
import jakarta.mail.MessagingException;

public interface ListenerService extends UserService {
    OAuth2Authentication findOAuth2ById(String id);

    void save(ListenerOAuthInfoRequest request);

    boolean isEmailExists(String email);

    boolean isUsernameExists(String username);

    OAuth2Authentication findOAuth2ByEmail(String email);

    String findIdByEmail(String email);

    boolean isLinkedOAuth2Provider(String email, OAuth2Type provider);

    void linkOAuth2ToListener(String email, OAuth2Type provider);

    void registerAndSendOtp(ListenerRegisterRequest request) throws MessagingException;

    ListenerResponse getById(String id);
}
