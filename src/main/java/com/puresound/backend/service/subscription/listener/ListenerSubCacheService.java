package com.puresound.backend.service.subscription.listener;

import com.puresound.backend.dto.subscription.listener.ListenerSubCache;
import com.puresound.backend.dto.subscription.listener.ListenerSubResponse;

public interface ListenerSubCacheService {
    ListenerSubCache get(String listenerId);

    void create(String listenerId, ListenerSubResponse subscription);
}
