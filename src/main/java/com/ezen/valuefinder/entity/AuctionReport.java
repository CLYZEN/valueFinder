package com.ezen.valuefinder.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "auction_report")
@ToString
@Getter
@Setter
public class AuctionReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long auctionReportNo; // 경매신고식별자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auction_no")
    private Auction auction; // 경매식별자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member; // 회원

    @Column(nullable = false)
    private String auctionReportTitle; // 경매신고제목

    @Column(nullable = false)
    private String auctionReportDetail; // 경매신고내용
}
