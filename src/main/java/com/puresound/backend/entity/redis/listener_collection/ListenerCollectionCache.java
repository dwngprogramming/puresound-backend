package com.puresound.backend.entity.redis.listener_collection;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(value = "listener_collection", timeToLive = 604800) // 7 days in seconds
@Getter
@Setter
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class ListenerCollectionCache {

    @Id
    @NotBlank(message = "INVALID_REQUEST")
    String listenerId;

    // Các trường trong class này lưu vào Redis & MySQL
    @NotNull(message = "INVALID_REQUEST")
    LibraryCache library;

    // Các trường trong class này chỉ cache vào Redis
    @NotNull(message = "INVALID_REQUEST")
    PlaybackHistoryCache playbackHistory;
}
