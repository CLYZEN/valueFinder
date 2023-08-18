package com.ezen.valuefinder.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Entity
@Table(name = "coupon_list")
@ToString
@Getter
@Setter
public class CouponList extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long couponListNo; // 쿠폰리스트식별자

    @Column(nullable = false)
    private String couponTitle; // 쿠폰제목

    @Column(nullable = false)
    private String couponDetail; // 쿠폰셜명

    @Column(nullable = false)
    private LocalDate startDate; // 유효기간 시작

    @Column(nullable = false)
    private LocalDate expireDate; // 유효기간 종료

    @Column(nullable = false)
    private Long couponDiscount; // 쿠폰 할인율
}
