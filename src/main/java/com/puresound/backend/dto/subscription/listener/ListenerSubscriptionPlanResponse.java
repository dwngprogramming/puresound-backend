package com.puresound.backend.dto.subscription.listener;

import com.puresound.backend.constant.payment.CurrencyCode;
import com.puresound.backend.constant.user.listener.ListenerBillingCycle;
import com.puresound.backend.constant.user.listener.ListenerSubscriptionType;

import java.math.BigDecimal;

public record ListenerSubscriptionPlanResponse(
        String name,
        String description,
        BigDecimal price,
        CurrencyCode currencyCode,
        ListenerBillingCycle billingCycle,
        ListenerSubscriptionType subscriptionType
) {
}
