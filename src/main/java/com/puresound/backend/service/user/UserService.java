package com.puresound.backend.service.user;

import com.puresound.backend.security.local.LocalAuthentication;

public interface UserService {
    LocalAuthentication findByUsernameOrEmail(String usernameOrEmail);
    LocalAuthentication findById(String id);
}
