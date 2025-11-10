package com.puresound.backend.service.cache.library;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class LibraryCache {
    List<SavedTrackCache> savedTracks;
    List<SavedAlbumCache> savedAlbums;
    List<FollowedArtistCache> followedArtists;
}
