package com.ezen.valuefinder.entity;

import com.ezen.valuefinder.constant.BidStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "success_bidding")
@ToString
@Getter
@Setter
public class SuccessBidding extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long successBiddingNo; // 낙찰식별자

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auction_no")
    private Auction auction; // 경매

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member; // 회원

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BidStatus bidStatus; // 낙찰상태

    private String shippingNo; // 운송장번호
}
