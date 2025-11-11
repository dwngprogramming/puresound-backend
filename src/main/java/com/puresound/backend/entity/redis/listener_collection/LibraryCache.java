package com.puresound.backend.entity.redis.listener_collection;

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
