package com.puresound.backend.dto.pagination;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Sort;

public record SPFRequest(
        // Not display validation, because SPFRequest is created internally
        @NotNull(message = "INTERNAL_SERVER_ERROR")
        @Min(0)
        Integer page,

        @NotNull(message = "INTERNAL_SERVER_ERROR")
        @Min(1)
        Integer size,

        @NotNull(message = "INTERNAL_SERVER_ERROR")
        Sort sort
) {
    public static SPFRequest of(Integer page, Integer size, String sortBy, String sortDir) {
        Sort.Direction sortDirection = parseSortDirection(sortDir);
        Sort sort = Sort.by(sortDirection, sortBy);
        return new SPFRequest(page, size, sort);
    }

    public static SPFRequest withPopularityDefaultSort(Integer page, Integer size) {
        return new SPFRequest(page, size, Sort.by(Sort.Direction.DESC, "popularity"));
    }

    public static SPFRequest withCreatedAtDefaultSort(Integer page, Integer size) {
        return new SPFRequest(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    public static Sort.Direction parseSortDirection(String sortDir) {
        if (sortDir != null && !sortDir.isBlank()) {
            sortDir = sortDir.trim();
            if (sortDir.equalsIgnoreCase("asc")) {
                return Sort.Direction.ASC;
            }
        }
        return Sort.Direction.DESC;
    }
}
