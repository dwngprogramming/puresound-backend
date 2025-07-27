package com.puresound.backend.service.user.router;

import com.puresound.backend.constant.user.UserType;
import com.puresound.backend.service.user.UserService;

public interface UserServiceRouter {
    UserService resolve(UserType userType);
}
