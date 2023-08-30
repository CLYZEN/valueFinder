package com.ezen.valuefinder.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "auction_review")
@ToString
@Getter
@Setter
public class AuctionReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long auctionReviewNo; // 경매후기식별자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member; // 회원

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auction_no")
    private Auction auction; // 경매

    @Column(nullable = false)
    private String auctionReviewTitle; // 경매후기제목

    @Column(nullable = false)
    private String auctionReviewDetail; // 경매후기상세

    @Column(nullable = false)
    private Long auctionReviewScore; // 경매후기별점
}
