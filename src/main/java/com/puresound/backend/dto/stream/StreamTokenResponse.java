package com.puresound.backend.dto.stream;

public record StreamTokenResponse(
        String token,
        Long exp
) {
    public static StreamTokenResponse create(String token, Long exp) {
        return new StreamTokenResponse(token, exp);
    }
}
