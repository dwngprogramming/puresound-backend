package com.puresound.backend.dto.pagination;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.puresound.backend.dto.metadata.album.SimplifiedAlbumResponse;
import com.puresound.backend.dto.metadata.artist.SimplifiedArtistResponse;
import com.puresound.backend.dto.metadata.track.SimplifiedTrackResponse;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record SearchResponse(
        List<SimplifiedTrackResponse> tracks,
        List<SimplifiedAlbumResponse> albums,
        List<SimplifiedArtistResponse> artists
) {
    public static SearchResponse of(List<SimplifiedTrackResponse> tracks, List<SimplifiedAlbumResponse> albums, List<SimplifiedArtistResponse> artists) {
        return new SearchResponse(tracks, albums, artists);
    }

    public static SearchResponse justArtists(List<SimplifiedArtistResponse> artists) {
        return new SearchResponse(null, null, artists);
    }

    public static SearchResponse justAlbums(List<SimplifiedAlbumResponse> albums) {
        return new SearchResponse(null, albums, null);
    }

    public static SearchResponse justTracks(List<SimplifiedTrackResponse> tracks) {
        return new SearchResponse(tracks, null, null);
    }

    public static SearchResponse withTracksAndAlbums(List<SimplifiedTrackResponse> tracks, List<SimplifiedAlbumResponse> albums) {
        return new SearchResponse(tracks, albums, null);
    }

    public static SearchResponse withTracksAndArtists(List<SimplifiedTrackResponse> tracks, List<SimplifiedArtistResponse> artists) {
        return new SearchResponse(tracks, null, artists);
    }

    public static SearchResponse withAlbumsAndArtists(List<SimplifiedAlbumResponse> albums, List<SimplifiedArtistResponse> artists) {
        return new SearchResponse(null, albums, artists);
    }

    public static SearchResponse empty() {
        return new SearchResponse(null, null, null);
    }
}
