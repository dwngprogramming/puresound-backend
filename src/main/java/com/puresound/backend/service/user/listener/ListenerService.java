package com.puresound.backend.service.user.listener;

import com.puresound.backend.dto.listener.ListenerOAuthInfoRequest;
import com.puresound.backend.security.oauth2.OAuth2Authentication;
import com.puresound.backend.service.user.UserService;

public interface ListenerService extends UserService {
    OAuth2Authentication findOAuth2ById(String id);
    void save(ListenerOAuthInfoRequest request);
    boolean isEmailExists(String email);
}
