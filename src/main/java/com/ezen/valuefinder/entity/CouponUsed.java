package com.ezen.valuefinder.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "coupon_used")
@ToString
@Getter
@Setter
public class CouponUsed extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long couponUsedNo; // 쿠폰사용내역식별자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_no")
    private Coupon coupon; // 쿠폰

    @Column(nullable = false)
    private LocalDateTime couponUsedDate; // 쿠폰사용일자
}
