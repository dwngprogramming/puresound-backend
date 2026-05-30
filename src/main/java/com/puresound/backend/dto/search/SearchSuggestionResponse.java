package com.puresound.backend.dto.search;

import com.puresound.backend.dto.metadata.album.SimplifiedAlbumResponse;
import com.puresound.backend.dto.metadata.artist.SimplifiedArtistResponse;
import com.puresound.backend.dto.metadata.track.SimplifiedTrackResponse;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record SearchSuggestionResponse(
        @Schema(description = "Normalized keyword used for suggestion lookup", example = "love")
        String keyword,

        @Schema(description = "Track suggestions matching the keyword")
        List<SimplifiedTrackResponse> tracks,

        @Schema(description = "Album suggestions matching the keyword")
        List<SimplifiedAlbumResponse> albums,

        @Schema(description = "Artist suggestions matching the keyword")
        List<SimplifiedArtistResponse> artists
) {
    public static SearchSuggestionResponse of(String keyword,
                                              List<SimplifiedTrackResponse> tracks,
                                              List<SimplifiedAlbumResponse> albums,
                                              List<SimplifiedArtistResponse> artists) {
        return new SearchSuggestionResponse(keyword, tracks, albums, artists);
    }
}
