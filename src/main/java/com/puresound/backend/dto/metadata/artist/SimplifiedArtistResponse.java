package com.puresound.backend.dto.metadata.artist;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.puresound.backend.dto.image.ImageResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder(toBuilder = true)
public record SimplifiedArtistResponse(
        @Schema(description = "Unique identifier of the artist", example = "01K8NFPNB5S4KAM5MQWMRMT086")
        String id,

        @Schema(description = "Stage name of the artist", example = "OPALS")
        String stageName,

        @Schema(description = "Profile images (square, 3 sizes) of the artist")
        List<ImageResponse> images
) {
}
