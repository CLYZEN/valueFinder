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
    @JoinColumn(name = "member_id")
    private Member member; // 회원

    @Column(nullable = false)
    private String couponTitle; // 쿠폰제목

    @Column(nullable = false)
    private String couponCode; // 쿠폰코드

    @Column(nullable = false)
    private LocalDateTime couponExpireStart; // 쿠폰유효기간 시작일

    @Column(nullable = false)
    private LocalDateTime couponExpire; // 쿠폰유효기간 종료일

    @Column(nullable = false)
    private LocalDateTime couponRegistDate; // 쿠폰등록일

    @Column(nullable = false)
    private String couponDetail; // 쿠폰설명

    @Column(nullable = false)
    private Long couponDiscount; // 쿠폰 할인율
}
