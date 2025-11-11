package com.puresound.backend.entity.jpa.subscription.listener;

import com.puresound.backend.entity.jpa.Base;
import com.puresound.backend.entity.jpa.payment.listener.ListenerPayment;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

@Entity
@Table(name = "listener_subscription_periods")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ListenerSubPeriod extends Base {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscription_id", nullable = false)
    ListenerSub subscription;

    @Column(name = "period", nullable = false)
    Integer period;

    @Column(name = "start_date", nullable = false, columnDefinition = "DATETIME(6)")
    Instant startDate;

    @Column(name = "end_date", nullable = false, columnDefinition = "DATETIME(6)")
    Instant endDate;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id")
    ListenerPayment payment;
}
