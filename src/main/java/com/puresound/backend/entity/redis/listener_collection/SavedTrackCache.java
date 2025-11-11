package com.puresound.backend.entity.redis.listener_collection;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class SavedTrackCache {
    String trackId;
    Long savedAt;
    Long playedAt;
}
