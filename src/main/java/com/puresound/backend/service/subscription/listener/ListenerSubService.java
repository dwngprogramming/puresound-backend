package com.puresound.backend.service.subscription.listener;

import com.puresound.backend.dto.subscription.BasicSubResponse;
import com.puresound.backend.dto.subscription.listener.ListenerSubResponse;

public interface ListenerSubService {
    ListenerSubResponse getCurrentDetailByListenerId(String listenerId);

    BasicSubResponse getCurrentBasicByListenerId(String listenerId);
}
