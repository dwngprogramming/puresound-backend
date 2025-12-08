package com.puresound.backend.dto.pagination;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record SPFResponse<T>(
        @Schema(description = "List of items in the current page")
        List<T> content,

        @Schema(description = "Paging information")
        PagingResponse paging
) {
    public static <T> SPFResponse<T> of(List<T> content, PagingResponse paging) {
        return new SPFResponse<>(
                content,
                paging
        );
    }
}
