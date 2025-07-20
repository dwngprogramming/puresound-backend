package com.puresound.backend.exception;

import com.puresound.backend.constant.api.ApiMessage;
import com.puresound.backend.dto.ApiResponse;
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

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    MessageSource messageSource;

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ApiResponse<Void>> handleApplicationException(ApplicationException e, Locale locale) {
        // With log, automatically using English
        log.error("Exception from ApplicationException - Code: {}, Message: {}",
                e.getApiMessage().name(), messageSource.getMessage(e.getMessage(), null, Locale.ENGLISH));
        return ResponseEntity
                .status(e.getStatus())
                .body(buildResponse(e.getApiMessage(), locale));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGeneralException(Exception e, Locale locale) {
        log.error("Unexpected error occurred: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(buildResponse(ApiMessage.INTERNAL_SERVER_ERROR, locale));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationException(MethodArgumentNotValidException ex, Locale locale) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        log.warn("Validation failed: {}", errors);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(buildResponse(ApiMessage.INVALID_REQUEST, errors, locale));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex, Locale locale) {
        log.error("Type mismatch: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(buildResponse(ApiMessage.MISMATCH_REQUEST, locale));
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleUsernameNotFoundException(UsernameNotFoundException ex, Locale locale) {
        log.error("User not found: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(buildResponse(ApiMessage.LOGIN_WRONG_INFO, locale));
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNoResourceFoundException(NoResourceFoundException ex, Locale locale) {
        log.error("Resource not found: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(buildResponse(ApiMessage.SYSTEM_RESOURCE_NOT_FOUND, locale));
    }

    private ApiResponse<Void> buildResponse(ApiMessage apiMessage, Locale locale) {
        return new ApiResponse<>(apiMessage.name(), messageSource.getMessage(apiMessage.name(), null, locale));
    }

    private <T> ApiResponse<T> buildResponse(ApiMessage apiMessage, T data, Locale locale) {
        return new ApiResponse<>(apiMessage.name(), messageSource.getMessage(apiMessage.name(), null, locale), data);
    }
}
