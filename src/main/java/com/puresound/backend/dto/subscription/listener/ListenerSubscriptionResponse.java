package com.puresound.backend.dto.subscription.listener;

import com.puresound.backend.constant.user.listener.SubscriptionStatus;

public record ListenerSubscriptionResponse(
        ListenerSubscriptionPlanResponse plan,
        SubscriptionStatus subsStatus,
        Boolean autoRenew,
        Integer totalPeriod
) {
}
