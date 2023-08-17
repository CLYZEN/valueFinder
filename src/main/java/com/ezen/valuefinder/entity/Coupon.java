package com.ezen.valuefinder.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "coupon")
@ToString
@Getter
@Setter
public class Coupon extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long couponNo; // 쿠폰식별자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_list_no")
    private CouponList couponList;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member; // 회원

    @Column(nullable = false)
    private LocalDateTime couponRegistDate; // 쿠폰등록일

    private LocalDateTime couponUsedDate; // 쿠폰사용일
}
