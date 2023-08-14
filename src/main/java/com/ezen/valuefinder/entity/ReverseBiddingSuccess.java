package com.ezen.valuefinder.entity;

import com.ezen.valuefinder.constant.BidStatus;
import jakarta.persistence.*;

@Entity
@Table(name = "reverse_bidding_success")
public class ReverseBiddingSuccess extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reverseBiddingSuccessNo; // 역경매낙찰식별자

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reverse_bidding_no")
    private ReverseBidding reverseBidding; // 역경매

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member; // 회원

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BidStatus bidStatus; // 낙찰상태

    private String shippingNo; // 운송장번호
}
