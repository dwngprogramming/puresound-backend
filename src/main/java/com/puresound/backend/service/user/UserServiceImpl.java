package com.puresound.backend.service.user;

import com.puresound.backend.security.local.LocalAuthentication;
import com.puresound.backend.security.oauth2.OAuth2Authentication;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
@Slf4j
public class UserServiceImpl implements UserService {
    static Pattern USERNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9\\-_.]{6,30}$");
    static Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");

    public boolean isUsername(String usernameOrEmail) {
        return usernameOrEmail != null && USERNAME_PATTERN.matcher(usernameOrEmail).matches();
    }

    public boolean isEmail(String usernameOrEmail) {
        return usernameOrEmail != null && EMAIL_PATTERN.matcher(usernameOrEmail).matches();
    }

    @Override
    public LocalAuthentication findByUsernameOrEmail(String usernameOrEmail) {
        log.warn("Not implemented method findByUsernameOrEmail in UserServiceImpl");
        throw new UnsupportedOperationException("Not implemented this method in UserServiceImpl");
    }

    @Override
    public LocalAuthentication findById(String id) {
        log.warn("Not implemented method findById in UserServiceImpl");
        throw new UnsupportedOperationException("Not implemented this method in UserServiceImpl");
    }
}
