package com.puresound.backend.dto.metadata.album;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.puresound.backend.constant.metadata.AlbumType;
import com.puresound.backend.dto.metadata.artist.ArtistResponse;

import java.time.LocalDate;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record BasicAlbumResponse(
        String id,
        String name,
        AlbumType albumType,
        LocalDate releaseDate,
        List<ArtistResponse> artists
) {
    public BasicAlbumResponse withArtists(List<ArtistResponse> artists) {
        return new BasicAlbumResponse(
                this.id,
                this.name,
                this.albumType,
                this.releaseDate,
                artists
        );
    }
}
