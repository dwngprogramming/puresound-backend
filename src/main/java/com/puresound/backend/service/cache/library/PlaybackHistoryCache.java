package com.puresound.backend.service.cache.library;

import com.puresound.backend.constant.LoopMode;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class PlaybackHistoryCache {
    String lastTrackId;
    Long lastPlayedPositionMs;
    Long lastPlayedAt;
    LoopMode loopMode;
    boolean isShuffled;
    List<String> recentlyTrackIds;
}
