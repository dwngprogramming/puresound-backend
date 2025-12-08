package com.puresound.backend.dto.pagination;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class PagingResponse {
    @Schema(description = "Current page number (1-based index)", example = "1")
    Integer page;

    @Schema(description = "Number of items per page (default is 10)", example = "10")
    Integer size;

    @Schema(description = "Indicates if there is first page", example = "true")
    Boolean first;

    @Schema(description = "Indicates if there is last page", example = "false")
    Boolean last;

    @Schema(description = "Number of items in the current page", example = "10")
    Integer numberOfElements;

    @Schema(description = "Total number of pages", example = "5")
    Integer totalPages;

    @Schema(description = "Total number of items across all pages", example = "50")
    Long totalElements;

    @Schema(description = "Sort direction (ASC or DESC)", example = "DESC")
    String sortDirection;

    @Schema(description = "Field by which the results are sorted", example = "popularity")
    String sortBy;
}
