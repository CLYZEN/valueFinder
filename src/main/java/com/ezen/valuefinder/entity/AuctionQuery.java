package com.ezen.valuefinder.entity;

import com.ezen.valuefinder.constant.AuctionQueryDistinction;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "auction_query")
@ToString
@Getter
@Setter
public class AuctionQuery extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long auctionQueryNo; // 경매문의식별자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auction_no")
    private Auction auction; // 경매

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member; // 회원

    @Column(nullable = false)
    private String auctionQueryTitle; // 경매문의제목

    @Column(nullable = false)
    private String auctionQueryDetail; // 경매문의상세

    @Enumerated(EnumType.STRING)
    private AuctionQueryDistinction auctionQueryDistinction; // 경매문의구분

}
