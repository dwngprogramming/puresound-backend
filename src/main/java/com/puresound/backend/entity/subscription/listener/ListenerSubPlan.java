package com.puresound.backend.entity.subscription.listener;

import com.puresound.backend.constant.payment.Currency;
import com.puresound.backend.constant.user.Eligibility;
import com.puresound.backend.constant.user.listener.BillingCycle;
import com.puresound.backend.constant.user.listener.SubscriptionType;
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
public class ListenerSubPlan extends Base {
    @Enumerated(value = EnumType.STRING)
    @Column(name = "subscription_type", nullable = false)
    SubscriptionType subscriptionType;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "billing_cycle", nullable = false)
    BillingCycle billingCycle;

    @Column(nullable = false, precision = 10, scale = 2)
    BigDecimal price;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "currency", nullable = false)
    Currency currency;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "eligibility", nullable = false)
    Eligibility eligibility;
}
