package com.puresound.backend.dto.subscription;

import com.puresound.backend.constant.user.listener.BillingCycle;
import com.puresound.backend.constant.user.listener.SubscriptionStatus;
import com.puresound.backend.constant.user.listener.SubscriptionType;

public record BasicSubResponse(
        BillingCycle billingCycle,
        SubscriptionType subscriptionType,
        SubscriptionStatus subscriptionStatus
) {
    public static BasicSubResponse empty() {
        return new BasicSubResponse(
                null,
                null,
                null
        );
    }
}
