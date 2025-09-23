package com.puresound.backend.dto.subscription.listener;

import com.puresound.backend.constant.payment.Currency;
import com.puresound.backend.constant.user.listener.BillingCycle;
import com.puresound.backend.constant.user.listener.SubscriptionType;

import java.math.BigDecimal;

public record ListenerSubPlanResponse(
        String id,
        BillingCycle billingCycle,
        SubscriptionType subscriptionType,
        BigDecimal price,
        Currency currency
) {
}
