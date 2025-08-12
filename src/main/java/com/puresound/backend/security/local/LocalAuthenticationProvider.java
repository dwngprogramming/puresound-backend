package com.puresound.backend.security.local;

import com.puresound.backend.constant.api.ApiMessage;
import com.puresound.backend.constant.api.LogLevel;
import com.puresound.backend.exception.exts.BadRequestException;
import com.puresound.backend.security.jwt.UserPrincipal;
import com.puresound.backend.service.user.UserService;
import com.puresound.backend.service.user.router.UserServiceRouter;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Component
public class LocalAuthenticationProvider implements AuthenticationProvider {
    PasswordEncoder passwordEncoder;
    UserServiceRouter router;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        LocalAuthenticationToken token = (LocalAuthenticationToken) authentication;

        UserService userService = router.resolve(token.getUserType());
        LocalAuthentication authInfo = userService.loginByUsernameOrEmail(token.getUsernameOrEmail());

        if (!passwordEncoder.matches(token.getPassword(), authInfo.password())) {
            throw new BadRequestException(ApiMessage.LOGIN_WRONG_INFO, LogLevel.INFO);
        }

        List<GrantedAuthority> authorities = authInfo.roles().stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        UserPrincipal principal = new UserPrincipal(authInfo.id(), authInfo.fullname(), authInfo.userType(), authInfo.roles());

        return new LocalAuthenticationToken(principal, authorities);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return LocalAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
