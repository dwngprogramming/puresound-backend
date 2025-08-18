package com.puresound.backend.config.audit;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Component
public class UnauthenticatedAuditorContextHolder {
    static final ThreadLocal<String> CURRENT_AUDITOR = new ThreadLocal<>();

    public static void setCurrentAuditor(String auditor) {
        CURRENT_AUDITOR.set(auditor);
    }

    public static String getCurrentAuditor() {
        return CURRENT_AUDITOR.get();
    }

    public static void clear() {
        CURRENT_AUDITOR.remove();
    }
}
