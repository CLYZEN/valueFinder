package com.ezen.valuefinder.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

import com.ezen.valuefinder.constant.BidStatus;

@Entity
@Table(name = "bidding")
@ToString
@Getter
@Setter
public class Bidding extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long biddingNo; // 입찰식별자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auction_no")
    private Auction auction; // 경매

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member; // 회원

    @Column(nullable = false)
    private Long biddingPrice; // 입찰가격

    @Column(nullable = false)
    private LocalDateTime biddingTime; // 입찰시간
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BidStatus bidStatus;
}
