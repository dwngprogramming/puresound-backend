package com.puresound.backend.dto.metadata.artist;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ArtistResponse(
        String id,
        String stageName,
        String bio,
        String country,
        String locale,
        Long followers,
        Integer popularity,
        Boolean verified
) {
}
