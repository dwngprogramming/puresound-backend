package com.puresound.backend.exception;

import com.puresound.backend.constant.api.ApiMessage;
import com.puresound.backend.dto.ApiResponse;
import com.puresound.backend.util.LogFactory;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import com.puresound.backend.util.ApiResponseFactory;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@RestControllerAdvice
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class GlobalExceptionHandler {
    MessageSource messageSource;
    ApiResponseFactory apiResponseFactory;

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ApiResponse<Void>> handleApplicationException(ApplicationException e, Locale locale) {
        LogFactory.createApplicationLog(e.getLogLevel(), e.getApiMessage(), messageSource, e);
        return ResponseEntity
                .status(e.getStatus())
                .body(apiResponseFactory.create(e.getApiMessage(), locale));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGeneralException(Exception e, Locale locale) {
        log.error("Unexpected error occurred: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(apiResponseFactory.create(ApiMessage.INTERNAL_SERVER_ERROR, locale));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationException(MethodArgumentNotValidException ex, Locale locale) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        log.warn("Validation failed: {}", errors);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(apiResponseFactory.create(ApiMessage.INVALID_REQUEST, errors, locale));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex, Locale locale) {
        log.error("Type mismatch: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(apiResponseFactory.create(ApiMessage.MISMATCH_REQUEST, locale));
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleUsernameNotFoundException(UsernameNotFoundException ex, Locale locale) {
        log.error("User not found: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(apiResponseFactory.create(ApiMessage.LOGIN_WRONG_INFO, locale));
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNoResourceFoundException(NoResourceFoundException ex, Locale locale) {
        log.error("Resource not found: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(apiResponseFactory.create(ApiMessage.SYSTEM_RESOURCE_NOT_FOUND, locale));
    }
}
