package com.puresound.backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

public record ApiResponse<T>(
        @Schema(description = "Response code of the API call", example = "200")
        String code,

        @Schema(description = "Response message of the API call", example = "Request processed successfully")
        String message,

        @Schema(description = "Response data of the API call")
        @JsonInclude(JsonInclude.Include.NON_NULL)
        T data
) {
    public ApiResponse(String code, String message) {
        this(code, message, null);
    }
}
