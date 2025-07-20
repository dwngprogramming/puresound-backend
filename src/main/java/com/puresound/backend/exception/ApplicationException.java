package com.puresound.backend.exception;

import com.puresound.backend.constant.api.ApiMessage;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Getter
public class ApplicationException extends RuntimeException {
    ApiMessage apiMessage;
    HttpStatus status;

    protected ApplicationException(ApiMessage apiMessage, HttpStatus status) {
        super(apiMessage.name());
        this.apiMessage = apiMessage;
        this.status = status;
    }
}
