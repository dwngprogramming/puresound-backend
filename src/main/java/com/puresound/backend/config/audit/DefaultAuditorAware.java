package com.puresound.backend.config.audit;

import com.puresound.backend.security.jwt.UserPrincipal;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class DefaultAuditorAware implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
        // Check unauthenticated first
        String unauthenticatedAuditor = UnauthenticatedAuditorContextHolder.getCurrentAuditor();
        if (unauthenticatedAuditor != null) {
            return Optional.of(unauthenticatedAuditor);
        }

        // Then check authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserPrincipal principal) {
            return Optional.of(principal.email());
        }
        return Optional.of("ANONYMOUS");
    }
}
