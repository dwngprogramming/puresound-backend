package com.puresound.backend.service.subscription.listener;

import com.puresound.backend.dto.subscription.listener.ListenerSubResponse;

public interface ListenerSubService {
    ListenerSubResponse getCurrentByListenerId(String listenerId);
}
