package com.puresound.backend.service.cache;

import com.puresound.backend.entity.redis.listener_collection.ListenerCollectionCache;

public interface ListenerCollectionService {
    ListenerCollectionCache getById(String listenerId);

    void create(ListenerCollectionCache collection);

    boolean isExists(String listenerId);

    ListenerCollectionCache update(ListenerCollectionCache collection);
}
