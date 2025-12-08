package com.puresound.backend.dto.metadata.album;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.puresound.backend.constant.metadata.AlbumType;
import com.puresound.backend.dto.metadata.artist.SimplifiedArtistResponse;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record SimplifiedAlbumResponse(
        @Schema(description = "Unique identifier of the album", example = "01K8NFPVP9N9FN5BD6W4YVFBZQ")
        String id,

        @Schema(description = "Name of the album", example = "Hero's Ending")
        String name,

        @Schema(description = "Type of the album", example = "SINGLE")
        AlbumType albumType,

        @Schema(description = "Release date of the album", example = "2023-08-25")
        LocalDate releaseDate,

        @Schema(description = "List of simplified artist information associated with the album")
        List<SimplifiedArtistResponse> artists
) {
    public SimplifiedAlbumResponse withArtists(List<SimplifiedArtistResponse> artists) {
        return new SimplifiedAlbumResponse(
                this.id,
                this.name,
                this.albumType,
                this.releaseDate,
                artists
        );
    }
}
