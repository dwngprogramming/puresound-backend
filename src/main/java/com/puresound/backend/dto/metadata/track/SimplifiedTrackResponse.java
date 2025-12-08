package com.puresound.backend.dto.metadata.track;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.puresound.backend.dto.metadata.album.SimplifiedAlbumResponse;
import com.puresound.backend.dto.metadata.artist.SimplifiedArtistResponse;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record SimplifiedTrackResponse(

        @Schema(description = "Unique identifier of the track", example = "01K8NFN35729A5PHCQ7FNYG8BH")
        String id,

        @Schema(description = "Title of the track", example = "Song Title")
        String title,

        @Schema(description = "Indicates if the track has explicit content", example = "false")
        Boolean explicit,

        @Schema(description = "List of simplified artist information associated with the track")
        List<SimplifiedArtistResponse> artists,

        @Schema(description = "Simplified album information associated with the track")
        SimplifiedAlbumResponse album
) {
    public SimplifiedTrackResponse withArtistsAndAlbum(List<SimplifiedArtistResponse> artists, SimplifiedAlbumResponse album) {
        return new SimplifiedTrackResponse(
                this.id,
                this.title,
                this.explicit,
                artists,
                album
        );
    }
}
