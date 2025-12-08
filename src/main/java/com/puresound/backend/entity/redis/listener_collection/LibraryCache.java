package com.puresound.backend.entity.redis.listener_collection;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class LibraryCache {

    @NotNull(message = "INVALID_REQUEST")
    @Builder.Default
    List<SavedTrackCache> savedTracks = new ArrayList<>();

    @NotNull(message = "INVALID_REQUEST")
    @Builder.Default
    List<SavedAlbumCache> savedAlbums = new ArrayList<>();

    @NotNull(message = "INVALID_REQUEST")
    @Builder.Default
    List<FollowedArtistCache> followedArtists = new ArrayList<>();
}
