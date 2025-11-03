package com.puresound.backend.dto.metadata.album;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.puresound.backend.constant.metadata.AlbumType;
import com.puresound.backend.constant.metadata.DatePrecision;
import com.puresound.backend.dto.metadata.artist.ArtistResponse;
import com.puresound.backend.dto.metadata.track.TrackResponse;

import java.time.LocalDate;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record AlbumResponse(
        String id,
        String name,
        AlbumType albumType,
        Integer totalTracks,
        Long totalDurationMs,
        LocalDate releaseDate,
        String releaseTz,
        DatePrecision releaseDatePrecision,
        Integer popularity,
        List<ArtistResponse> artists,
        List<TrackResponse> tracks
) {

    public AlbumResponse withArtists(List<ArtistResponse> artists) {
        return new AlbumResponse(
                this.id,
                this.name,
                this.albumType,
                this.totalTracks,
                this.totalDurationMs,
                this.releaseDate,
                this.releaseTz,
                this.releaseDatePrecision,
                this.popularity,
                artists,
                this.tracks
        );
    }
}
