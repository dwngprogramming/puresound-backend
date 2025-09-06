package com.puresound.backend.security.event;

import com.puresound.backend.security.jwt.UserPrincipal;
import com.puresound.backend.security.local.LocalAuthenticationToken;
import com.puresound.backend.security.oauth2.CustomOAuth2AuthenticationToken;
import com.puresound.backend.service.user.UserService;
import com.puresound.backend.service.user.router.UserServiceRouter;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@RequiredArgsConstructor
@Component
public class AuthenticationEventListener {
    UserServiceRouter router;

    @EventListener
    public void onAuthenticationSuccess(AuthenticationSuccessEvent event) {
        log.debug("Authentication success event: {}", event.getAuthentication().getClass().getSimpleName());

        // Chỉ cập nhật last login cho form-based login hoặc OAuth2 login
        // Không cập nhật cho JWT Resource Server authentication
        if (isDirectLogin(event)) {
            if (event.getAuthentication().getPrincipal() instanceof UserPrincipal principal) {
                UserService userService = router.resolve(principal.userType());
                log.info("Updating last login for {}: {}", principal.userType().name(), principal.id());
                userService.updateLastLogin(principal.id());
            }
        }
    }

    /**
     * Xác định xem sự kiện xác thực có phải là đăng nhập trực tiếp hay không
     * (form-based hoặc OAuth2 login, không phải JWT token authentication)
     * Nhận event trả về của LocalAuthenticationToken hoặc CustomOAuth2AuthenticationToken
     */
    private boolean isDirectLogin(AuthenticationSuccessEvent event) {
        Authentication authentication = event.getAuthentication();
        return authentication instanceof LocalAuthenticationToken ||
                authentication instanceof CustomOAuth2AuthenticationToken;
    }
}
