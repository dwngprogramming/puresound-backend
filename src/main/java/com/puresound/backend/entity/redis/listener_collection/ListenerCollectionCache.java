package com.puresound.backend.entity.redis.listener_collection;

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
    String listenerId;
    // Các trường trong class này lưu vào Redis & MySQL
    LibraryCache library;
    // Các trường trong class này chỉ cache vào Redis
    PlaybackHistoryCache playbackHistory;
}
