package com.puresound.backend.service.cache.library;

import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(value = "followed_artist")
@Getter
@Setter
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class FollowedArtistCache {

    @Id
    String artistId;
    Long savedAt;
    Long playedAt;
}
