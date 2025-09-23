package com.puresound.backend.dto.subscription.listener;

import java.util.List;

public record ListenerPlansResponse(
        List<ListenerSubPlanResponse> plans
) {
}
