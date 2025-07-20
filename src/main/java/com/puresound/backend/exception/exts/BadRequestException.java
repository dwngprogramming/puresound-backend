package com.puresound.backend.exception.exts;

import com.puresound.backend.constant.api.ApiMessage;
import com.puresound.backend.constant.api.LogLevel;
import com.puresound.backend.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class BadRequestException extends ApplicationException {
    public BadRequestException(ApiMessage apiMessage, LogLevel logLevel) {
        super(apiMessage, HttpStatus.BAD_REQUEST, logLevel);
    }
}
