package com.puresound.backend.security.local;

import com.puresound.backend.constant.user.UserType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Getter
public class LocalAuthenticationToken extends AbstractAuthenticationToken {
    String usernameOrEmail;
    String password;
    UserType userType;
    Object principal;

    // Not authenticated
    public LocalAuthenticationToken(String usernameOrEmail, String password, UserType userType) {
        super(null);
        this.usernameOrEmail = usernameOrEmail;
        this.password = password;
        this.userType = userType;
        this.principal = null;
        setAuthenticated(false);
    }

    // Authenticated
    public LocalAuthenticationToken(Object principal, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.usernameOrEmail = null;
        this.password = null;
        this.userType = null;
        this.principal = principal;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return password;
    }

    @Override
    public Object getPrincipal() {
        return principal != null ? principal : usernameOrEmail;
    }
}

