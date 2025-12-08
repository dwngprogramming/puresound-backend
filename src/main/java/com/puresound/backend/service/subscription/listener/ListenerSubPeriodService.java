package com.puresound.backend.service.subscription.listener;

import com.puresound.backend.entity.jpa.subscription.listener.ListenerSubPeriod;

public interface ListenerSubPeriodService {
    ListenerSubPeriod getCurrentByListenerId(String listenerId);
}
