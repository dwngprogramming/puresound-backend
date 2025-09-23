package com.puresound.backend.service.subscription.listener;

import com.puresound.backend.dto.subscription.BasicSubResponse;
import com.puresound.backend.dto.subscription.listener.ListenerSubPlanResponse;
import com.puresound.backend.dto.subscription.listener.ListenerSubResponse;

import java.util.List;

public interface ListenerSubService {
    ListenerSubResponse getCurrentDetailByListenerId(String listenerId);

    BasicSubResponse getCurrentBasicByListenerId(String listenerId);

    List<ListenerSubPlanResponse> getAllPlans(boolean isFirstSubscription);
}
