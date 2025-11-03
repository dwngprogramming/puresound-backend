package com.puresound.backend.dto.metadata.track;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.puresound.backend.dto.metadata.album.BasicAlbumResponse;
import com.puresound.backend.dto.metadata.artist.ArtistResponse;
import com.puresound.backend.dto.metadata.genre.GenreResponse;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record TrackResponse(
        String id,
        String title,
        Long durationMs,
        Boolean explicit,
        Integer trackNumber,
        Integer popularity,
        Boolean isLocal,
        List<Integer> availableBitrates,
        List<ArtistResponse> artists,
        BasicAlbumResponse album,
        List<GenreResponse> genres
) {
    public TrackResponse withAdditionalData(List<ArtistResponse> artists,
                                            BasicAlbumResponse album,
                                            List<Integer> availableBitrates) {
        return new TrackResponse(
                this.id,
                this.title,
                this.durationMs,
                this.explicit,
                this.trackNumber,
                this.popularity,
                this.isLocal,
                availableBitrates,
                artists,
                album,
                this.genres
        );
    }
}
