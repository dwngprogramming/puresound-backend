package com.puresound.backend.entity.redis.listener_collection;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.puresound.backend.constant.LoopMode;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class PlaybackHistoryCache {

    @NotNull(message = "INVALID_REQUEST")
    String lastTrackId;

    @NotNull(message = "INVALID_REQUEST")
    @Positive(message = "INVALID_REQUEST")
    Long lastPlayedPosition;

    @NotNull(message = "INVALID_REQUEST")
    @PastOrPresent(message = "INVALID_REQUEST")
    @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
    Instant lastPlayedAt;

    @NotNull(message = "INVALID_REQUEST")
    LoopMode loopMode;

    boolean isShuffled;

    @NotNull(message = "INVALID_REQUEST")
    @Builder.Default
    List<String> recentlyTrackIds = new ArrayList<>();
}
