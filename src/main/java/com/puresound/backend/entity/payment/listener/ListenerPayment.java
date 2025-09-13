package com.puresound.backend.entity.payment.listener;

import com.puresound.backend.constant.payment.Currency;
import com.puresound.backend.constant.payment.PaymentGateway;
import com.puresound.backend.constant.payment.PaymentStatus;
import com.puresound.backend.entity.Base;
import com.puresound.backend.entity.coupon.ListenerCoupon;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Entity
@Table(name = "listener_payments")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ListenerPayment extends Base {

    @Column(name = "plan_id", nullable = false)
    String planId;    // Snapshot, phòng TH đăng ký lần đầu nhưng không thanh toán

    @Column(name = "amount", nullable = false, precision = 10, scale = 2)
    BigDecimal originalAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id")
    ListenerCoupon coupon;  // Có thể null nếu không áp dụng coupon

    @Column(name = "actual_amount", nullable = false, precision = 10, scale = 2)
    BigDecimal actualAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "currency", nullable = false)
    Currency currency;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_gateway", nullable = false)
    PaymentGateway paymentGateway;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false)
    @Builder.Default
    PaymentStatus paymentStatus = PaymentStatus.PENDING;
}
