package com.puresound.backend.util;

import com.puresound.backend.constant.api.ApiMessage;
import com.puresound.backend.constant.api.LogLevel;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;

import java.util.Locale;

@UtilityClass
@Slf4j
public class LogFactory {
    public static void createApplicationLog(LogLevel logLevel, ApiMessage apiMessage, MessageSource messageSource, Exception ex) {
        StackTraceElement thrower = ex.getStackTrace()[0];
        // With log, automatically using English
        String message = messageSource.getMessage(apiMessage.name(), null, Locale.ENGLISH);
        String detail = ex.getMessage();
        String throwerInfo = String.format("%s.%s(%s:%d)",
                thrower.getClassName(),
                thrower.getMethodName(),
                thrower.getFileName(),
                thrower.getLineNumber());

        String logMessage = String.format("""
                        Exception from ApplicationException:
                        - Code: %s
                        - Message: %s
                        - Detail Log: %s
                        \tat %s""",
                apiMessage.name(), message, detail, throwerInfo);

        switch (logLevel) {
            case DEBUG -> log.debug(logMessage);
            case INFO -> log.info(logMessage);
            case WARN -> log.warn(logMessage);
            case ERROR -> log.error(logMessage);
        }
    }
}
