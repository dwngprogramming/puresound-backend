package com.puresound.backend.dto.metadata.genre;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record GenreResponse(
        String id,
        String name
) {
}
