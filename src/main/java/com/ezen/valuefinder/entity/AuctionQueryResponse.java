package com.ezen.valuefinder.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "auction_query_response")
@ToString
@Getter
@Setter
public class AuctionQueryResponse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long auctionQueryResponseNo; // 문의답변식별자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auction_query_no")
    private AuctionQuery auctionQuery; // 문의

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member; // 회원

    @Column(nullable = false)
    private String auctionQueryResponseTitle; // 문의답변제목

    @Column(nullable = false)
    private String auctionQueryResponseDetail; // 문의답변
}
