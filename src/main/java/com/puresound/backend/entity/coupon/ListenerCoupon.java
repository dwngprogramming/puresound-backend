package com.puresound.backend.entity.coupon;

import com.puresound.backend.constant.payment.CurrencyCode;
import com.puresound.backend.constant.payment.DiscountType;
import com.puresound.backend.entity.Base;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "listener_coupons")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ListenerCoupon extends Base {

    @Column(nullable = false, unique = true)
    String code;

    @Column(nullable = false, length = 100)
    String title;

    @Column(columnDefinition = "TEXT")
    String description;

    @Column(name = "discount_amount", nullable = false, precision = 10, scale = 2)
    BigDecimal discountAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "discount_type", nullable = false)
    DiscountType discountType;

    @Enumerated(EnumType.STRING)
    @Column(name = "currency_code")
    CurrencyCode currencyCode;

    @Column(name = "start_date", nullable = false, columnDefinition = "DATETIME(6)")
    Instant startDate;

    @Column(name = "end_date", nullable = false, columnDefinition = "DATETIME(6)")
    Instant endDate;

    @Column(name = "redemption_left", nullable = false)
    Integer redemptionLeft;
}
