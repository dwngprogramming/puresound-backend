package com.puresound.backend.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.proc.BadJWTException;
import com.puresound.backend.constant.api.ApiMessage;
import com.puresound.backend.constant.api.LogLevel;
import com.puresound.backend.dto.ApiResponse;
import com.puresound.backend.util.ApiResponseFactory;
import com.puresound.backend.util.LogFactory;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.jwt.JwtValidationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.Locale;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Component
@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    ApiResponseFactory apiResponseFactory;
    MessageSource messageSource;
    ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        Locale locale = LocaleContextHolder.getLocale();
        Throwable cause = authException.getCause();
        ApiMessage apiMessage = ApiMessage.INVALID_TOKEN;

        switch (cause) {
            case BadJWTException badJWT ->
                    LogFactory.createApplicationLog(LogLevel.ERROR, ApiMessage.INVALID_TOKEN, messageSource, badJWT);
            case JOSEException jose ->
                    LogFactory.createApplicationLog(LogLevel.ERROR, ApiMessage.INVALID_TOKEN, messageSource, jose);
            case ParseException parse ->
                    LogFactory.createApplicationLog(LogLevel.ERROR, ApiMessage.INVALID_FORMAT_TOKEN, messageSource, parse);
            case JwtValidationException jwt ->
                    LogFactory.createApplicationLog(LogLevel.INFO, ApiMessage.INVALID_TOKEN, messageSource, jwt);
            case null -> {
                if (authException instanceof InsufficientAuthenticationException) {
                    LogFactory.createApplicationLog(LogLevel.WARN, ApiMessage.UNAUTHENTICATED, messageSource, authException);
                    apiMessage = ApiMessage.UNAUTHENTICATED;
                } else {
                    LogFactory.createApplicationLog(LogLevel.ERROR, ApiMessage.INTERNAL_SERVER_ERROR, messageSource, authException);
                }
            }
            default ->
                    LogFactory.createApplicationLog(LogLevel.ERROR, ApiMessage.INTERNAL_SERVER_ERROR, messageSource, authException);
        }


        ApiResponse<Void> apiResponse = apiResponseFactory.create(apiMessage, locale);

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        // Thêm CORS headers
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        response.setHeader("Access-Control-Allow-Credentials", "true");

        objectMapper.writeValue(response.getWriter(), apiResponse);
    }
}
