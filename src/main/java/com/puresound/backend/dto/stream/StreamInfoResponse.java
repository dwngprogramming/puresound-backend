package com.puresound.backend.dto.stream;

public record StreamInfoResponse(
        String baseUrl,
        String streamUrl,
        String tokenParam,
        Long exp
) {
}
