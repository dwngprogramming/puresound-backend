package com.puresound.backend.mapper.subscription.listener;

import com.puresound.backend.constant.user.listener.ListenerBillingCycle;
import com.puresound.backend.constant.user.listener.ListenerSubscriptionType;
import com.puresound.backend.constant.user.listener.SubscriptionStatus;
import com.puresound.backend.dto.subscription.listener.ListenerSubResponse;
import com.puresound.backend.entity.subscription.listener.ListenerSubPeriod;

public abstract class ListenerSubPeriodDecorator implements ListenerSubPeriodMapper {

    @Override
    public ListenerSubResponse toResponse(ListenerSubPeriod period) {
        String listenerId = period.getSubscription().getListener().getId();
        String planName = period.getSubscription().getPlan().getName();
        ListenerSubscriptionType subscriptionType = period.getSubscription().getPlan().getSubscriptionType();
        ListenerBillingCycle billingCycle = period.getSubscription().getPlan().getBillingCycle();
        SubscriptionStatus subscriptionStatus = period.getSubscription().getSubsStatus();

        return new ListenerSubResponse(
                listenerId,
                planName,
                billingCycle,
                subscriptionType,
                subscriptionStatus,
                period.getStartDate(),
                period.getEndDate(),
                period.getSubscription().getAutoRenew(),
                period.getPeriod());
    }
}
