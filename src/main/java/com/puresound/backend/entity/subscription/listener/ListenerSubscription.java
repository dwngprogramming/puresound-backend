package com.puresound.backend.entity.subscription.listener;

import com.puresound.backend.constant.user.listener.SubscriptionStatus;
import com.puresound.backend.entity.Base;
import com.puresound.backend.entity.listener.Listener;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "listener_subscriptions")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ListenerSubscription extends Base {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "listener_id", nullable = false)
    Listener listener;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscription_plan_id", nullable = false)
    ListenerSubscriptionPlan plan;

    @Enumerated(EnumType.STRING)
    @Column(name = "subs_status", nullable = false)
    SubscriptionStatus subsStatus;

    @Column(name = "auto_renew", nullable = false)
    @Builder.Default
    Boolean autoRenew = true;

    @Column(name = "total_period", nullable = false)
    @Builder.Default
    Integer totalPeriod = 0;
}
