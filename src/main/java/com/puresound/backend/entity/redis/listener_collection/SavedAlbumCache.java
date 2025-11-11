package com.puresound.backend.entity.redis.listener_collection;

import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class SavedAlbumCache {

    @Id
    String albumId;
    Long savedAt;
    Long playedAt;
}
