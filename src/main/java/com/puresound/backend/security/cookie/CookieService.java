package com.puresound.backend.security.cookie;

import com.puresound.backend.exception.InternalServerException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Component
public class CookieService {
    @Value("${cookie.secure}")
    boolean secure;

    @Value("${cookie.domain}")
    String domain;

    @Value("${cookie.same-site}")
    String sameSite;

    public void setCookie(String key, String value, long maxAgeMins, HttpServletResponse response) {
        if (key == null || value == null) throw new InternalServerException("Cookie key or value cannot be null");

        long maxAgeSeconds = maxAgeMins * 60;
        ResponseCookie cookie = ResponseCookie.from(key, value)
                .httpOnly(true)
                .secure(secure)
                .maxAge(maxAgeSeconds)
                .path("/")
                .domain(domain)
                .sameSite(sameSite)
                .build();

        response.addHeader("Set-Cookie", cookie.toString());
    }

    public void setCookieWithHttpOnlyFalse(String key, String value, long maxAgeMins, HttpServletResponse response) {
        if (key == null || value == null) throw new InternalServerException("Cookie key or value cannot be null");

        long maxAgeSeconds = maxAgeMins * 60;
        ResponseCookie cookie = ResponseCookie.from(key, value)
                .httpOnly(false)
                .secure(secure)
                .maxAge(maxAgeSeconds)
                .path("/")
                .domain(domain)
                .sameSite(sameSite)
                .build();

        response.addHeader("Set-Cookie", cookie.toString());
    }

    public String getCookie(String key, HttpServletRequest request) {
        if (request.getCookies() == null) return null;

        return Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals(key))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
    }

    public void deleteCookie(String key, HttpServletResponse response) {
        setCookie(key, "", 0, response);
    }
}
