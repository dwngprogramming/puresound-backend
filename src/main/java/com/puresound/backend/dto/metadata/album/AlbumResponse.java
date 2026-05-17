package com.puresound.backend.dto.metadata.album;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.puresound.backend.constant.metadata.AlbumType;
import com.puresound.backend.constant.metadata.DatePrecision;
import com.puresound.backend.dto.image.ImageResponse;
import com.puresound.backend.dto.metadata.artist.SimplifiedArtistResponse;
import com.puresound.backend.dto.metadata.track.TrackResponse;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder(toBuilder = true)
public record AlbumResponse(
        String id,
        String name,
        AlbumType albumType,
        List<ImageResponse> images,
        Integer totalTracks,
        Long totalDurationMs,
        LocalDate releaseDate,
        String releaseTz,
        DatePrecision releaseDatePrecision,
        Integer popularity,
        List<SimplifiedArtistResponse> artists,
        List<TrackResponse> tracks
) {

    public AlbumResponse withArtists(List<SimplifiedArtistResponse> artists) {
        return new AlbumResponse(
                this.id,
                this.name,
                this.albumType,
                this.images,
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
