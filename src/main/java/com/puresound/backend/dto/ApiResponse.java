package com.puresound.backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

public record ApiResponse<T>(
        String code,
        String message,

        @JsonInclude(JsonInclude.Include.NON_NULL)
        T data
) {
    public ApiResponse(String code, String message) {
        this(code, message, null);
    }
}
