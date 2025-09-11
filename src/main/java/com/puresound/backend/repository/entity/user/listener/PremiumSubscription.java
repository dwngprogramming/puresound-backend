package com.puresound.backend.repository.entity.user.listener;

import com.puresound.backend.constant.user.SubscriptionType;
import com.puresound.backend.repository.entity.Base;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

@Entity
@Table(name = "premium_subscriptions")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PremiumSubscription extends Base {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "listener_id", nullable = false)
    Listener listener;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "subscription_type", nullable = false)
    SubscriptionType subscriptionType;

    @Column(name = "start_date", nullable = false, columnDefinition = "DATETIME(6)")
    Instant startDate;

    @Column(name = "end_date", nullable = false, columnDefinition = "DATETIME(6)")
    Instant endDate;
}
