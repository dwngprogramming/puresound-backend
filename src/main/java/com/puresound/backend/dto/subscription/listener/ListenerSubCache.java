package com.puresound.backend.dto.subscription.listener;

import com.puresound.backend.constant.user.listener.BillingCycle;
import com.puresound.backend.constant.user.listener.SubscriptionType;
import com.puresound.backend.constant.user.listener.SubscriptionStatus;

import java.time.Instant;

public record ListenerSubCache(
        String listenerId,
        SubscriptionType subscriptionType,
        BillingCycle billingCycle,
        SubscriptionStatus subscriptionStatus,
        Instant startDate,
        Instant endDate,
        Boolean autoRenew,
        Integer totalPeriod
) {
}
