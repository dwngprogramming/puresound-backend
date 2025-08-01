package com.puresound.backend.security.converters;

import com.puresound.backend.constant.user.UserType;
import com.puresound.backend.security.jwt.CustomJwtAuthenticationToken;
import com.puresound.backend.security.jwt.UserPrincipal;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JwtAuthenticationTokenConverter implements Converter<Jwt, AbstractAuthenticationToken> {
    RoleConverter roleConverter;

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        // Extract data from JWT
        String userId = jwt.getSubject();
        String fullname = jwt.getClaimAsString("fullname");
        String userType = jwt.getClaimAsString("userType");
        List<String> jwtAuthorities = jwt.getClaimAsStringList("authorities");

        Collection<GrantedAuthority> authorities = roleConverter.convert(jwt);
        UserPrincipal principal;

        if (authorities == null || authorities.isEmpty()) {
            principal = new UserPrincipal(userId, fullname, UserType.fromString(userType), List.of());
        } else {
            principal = new UserPrincipal(userId, fullname, UserType.fromString(userType), jwtAuthorities);
        }

        return new CustomJwtAuthenticationToken(jwt, authorities, principal);
    }
}
