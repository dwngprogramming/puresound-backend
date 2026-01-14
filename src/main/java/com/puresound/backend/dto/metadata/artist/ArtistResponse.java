package com.puresound.backend.dto.metadata.artist;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.puresound.backend.dto.image.ImageResponse;
import lombok.Builder;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder(toBuilder = true)
public record ArtistResponse(
        String id,
        String stageName,
        List<ImageResponse> images,
        String bio,
        String country,
        String locale,
        Long followers,
        Integer popularity,
        Boolean verified
) {
}
