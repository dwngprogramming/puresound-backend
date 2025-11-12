package com.puresound.backend.entity.redis.listener_collection;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Getter
@Setter
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class SavedAlbumCache {

    @NotNull(message = "INVALID_REQUEST")
    String albumId;

    @NotNull(message = "INVALID_REQUEST")
    @PastOrPresent(message = "INVALID_REQUEST")
    @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
    Instant savedAt;

    @NotNull
    @PastOrPresent(message = "INVALID_REQUEST")
    @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
    Instant playedAt;
}
