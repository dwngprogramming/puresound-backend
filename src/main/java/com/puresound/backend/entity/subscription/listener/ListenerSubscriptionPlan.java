package com.puresound.backend.entity.subscription.listener;

import com.puresound.backend.constant.payment.CurrencyCode;
import com.puresound.backend.constant.user.listener.ListenerBillingCycle;
import com.puresound.backend.constant.user.listener.ListenerSubscriptionType;
import com.puresound.backend.entity.Base;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Entity
@Table(name = "listener_subscription_plans")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ListenerSubscriptionPlan extends Base {
    @Column(nullable = false)
    String name;

    @Column(columnDefinition = "TEXT")
    String description;

    @Column(nullable = false, precision = 10, scale = 2)
    BigDecimal price;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "currency_code", nullable = false)
    CurrencyCode currencyCode;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "billing_cycle", nullable = false)
    ListenerBillingCycle billingCycle;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "subscription_type", nullable = false)
    ListenerSubscriptionType subscriptionType;
}
