package com.puresound.backend.security.jwt;

import com.puresound.backend.security.local.UserPrincipal;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Collection;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CustomJwtAuthenticationToken extends JwtAuthenticationToken {
    // Extends JwtAuthenticationToken to add custom principal
    UserPrincipal principal;

    public CustomJwtAuthenticationToken(Jwt jwt, Collection<? extends GrantedAuthority> authorities, UserPrincipal principal) {
        super(jwt, authorities, principal.id());
        this.principal = principal;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    public UserPrincipal getUserPrincipal() {
        return principal;
    }
}
