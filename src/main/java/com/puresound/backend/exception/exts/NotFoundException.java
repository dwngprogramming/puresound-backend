package com.puresound.backend.exception.exts;

import com.puresound.backend.constant.api.ApiMessage;
import com.puresound.backend.constant.api.LogLevel;
import com.puresound.backend.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class NotFoundException extends ApplicationException {
    public NotFoundException(ApiMessage apiMessage, LogLevel logLevel) {
        super(apiMessage, HttpStatus.NOT_FOUND, logLevel);
    }
}
