package com.puresound.backend.service.subscription.listener;

import com.puresound.backend.dto.subscription.listener.ListenerSubPlanResponse;

import java.util.List;

public interface ListenerSubPlanService {
    List<ListenerSubPlanResponse> getAllPlans(boolean isFirstSubscription);
}
