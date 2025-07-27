package com.puresound.backend.service.user.router.impl;

import com.puresound.backend.constant.user.UserType;
import com.puresound.backend.service.user.UserService;
import com.puresound.backend.service.user.router.UserServiceRouter;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.util.Map;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Component
public class UserServiceRouterImpl implements UserServiceRouter {
    Map<String, UserService> userServiceRouterMap;

    @Override
    public UserService resolve(UserType userType) {
        String key = userType.name();
        UserService service = userServiceRouterMap.get(key);
        if (service == null) {
            throw new IllegalArgumentException("Unsupported user type: " + userType);
        }
        return service;
    }
}
