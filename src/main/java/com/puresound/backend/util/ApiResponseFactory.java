package com.puresound.backend.util;

import com.puresound.backend.constant.api.ApiMessage;
import com.puresound.backend.dto.ApiResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Component
public class ApiResponseFactory {
    MessageSource messageSource;

    public ApiResponse<Void> create(ApiMessage apiMessage, Locale locale) {
        return new ApiResponse<>(apiMessage.name(), messageSource.getMessage(apiMessage.name(), null, locale));
    }

    public <T> ApiResponse<T> create(ApiMessage apiMessage, T data, Locale locale) {
        return new ApiResponse<>(apiMessage.name(), messageSource.getMessage(apiMessage.name(), null, locale), data);
    }
}
