package com.puresound.backend.dto.stream;

public record StreamSessionRequest(
        String id,
        Boolean premium,
        Long exp
) {
    public static StreamSessionRequest create(String id, Boolean premium, Long exp) {
        return new StreamSessionRequest(id, premium, exp);
    }
}
