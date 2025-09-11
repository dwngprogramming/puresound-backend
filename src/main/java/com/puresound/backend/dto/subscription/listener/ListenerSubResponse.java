package com.puresound.backend.dto.subscription.listener;

import com.puresound.backend.constant.user.listener.ListenerBillingCycle;
import com.puresound.backend.constant.user.listener.ListenerSubscriptionType;
import com.puresound.backend.constant.user.listener.SubscriptionStatus;

import java.time.Instant;

public record ListenerSubResponse(
        String listenerId,
        String planName,
        ListenerBillingCycle billingCycle,
        ListenerSubscriptionType subscriptionType,
        SubscriptionStatus subscriptionStatus,
        Instant startDate,
        Instant endDate,
        Boolean autoRenew,
        Integer totalPeriod
) {
}
