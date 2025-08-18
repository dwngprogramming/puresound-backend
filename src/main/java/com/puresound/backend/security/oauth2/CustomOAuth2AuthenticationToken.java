package com.puresound.backend.security.oauth2;

import com.puresound.backend.constant.user.OAuth2Type;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Getter
public class CustomOAuth2AuthenticationToken extends AbstractAuthenticationToken {
    Object principal;
    OAuth2Type provider;
    String registrationId;

    // Chưa xác thực - lúc mới nhận OAuth2 code
    public CustomOAuth2AuthenticationToken(OAuth2Type provider, String registrationId) {
        super(null);
        this.principal = null;
        this.provider = provider;
        this.registrationId = registrationId;
        setAuthenticated(false);
    }

    // Đã xác thực - sau khi đã có UserPrincipal
    public CustomOAuth2AuthenticationToken(Object principal,
                                           Collection<? extends GrantedAuthority> authorities,
                                           OAuth2Type provider,
                                           String registrationId) {
        super(authorities);
        this.principal = principal;
        this.provider = provider;
        this.registrationId = registrationId;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }
}
