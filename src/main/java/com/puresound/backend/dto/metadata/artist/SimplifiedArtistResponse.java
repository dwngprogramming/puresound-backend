package com.puresound.backend.dto.metadata.artist;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record SimplifiedArtistResponse(
        @Schema(description = "Unique identifier of the artist", example = "01K8NFPNB5S4KAM5MQWMRMT086")
        String id,

        @Schema(description = "Stage name of the artist", example = "OPALS")
        String stageName
) {
}
