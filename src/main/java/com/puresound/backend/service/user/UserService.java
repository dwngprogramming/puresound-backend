package com.puresound.backend.service.user;

import com.puresound.backend.dto.auth.RefreshAuthentication;
import com.puresound.backend.security.local.LocalAuthentication;

public interface UserService {
    LocalAuthentication loginByUsernameOrEmail(String usernameOrEmail);
    RefreshAuthentication findToRefreshById(String id);
    void activateAccount(String email);
}
