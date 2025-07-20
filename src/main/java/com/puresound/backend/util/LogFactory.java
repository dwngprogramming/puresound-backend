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
        String throwerInfo = thrower.getClassName() + "." + thrower.getMethodName() + "(" +
                thrower.getFileName() + ":" + thrower.getLineNumber() + ")";
        // With log, automatically using English
        switch (logLevel) {
            case DEBUG -> log.debug("Exception from ApplicationException - Code: {}, Message: {}, Thrower: {}",
                    apiMessage.name(), messageSource.getMessage(apiMessage.name(), null, Locale.ENGLISH), throwerInfo);
            case INFO -> log.info("Exception from ApplicationException - Code: {}, Message: {}, Caller: {}",
                    apiMessage.name(), messageSource.getMessage(apiMessage.name(), null, Locale.ENGLISH), throwerInfo);
            case WARN -> log.warn("Exception from ApplicationException - Code: {}, Message: {}, Caller: {}",
                    apiMessage.name(), messageSource.getMessage(apiMessage.name(), null, Locale.ENGLISH), throwerInfo);
            case ERROR -> log.error("Exception from ApplicationException - Code: {}, Message: {}, Caller: {}",
                    apiMessage.name(), messageSource.getMessage(apiMessage.name(), null, Locale.ENGLISH), throwerInfo);
        }
    }
}
