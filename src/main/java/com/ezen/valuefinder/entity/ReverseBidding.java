package com.ezen.valuefinder.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

import com.ezen.valuefinder.constant.ReversebidAuctionStatus;

@Entity
@Table(name = "reverse_bidding")
@ToString
@Getter
@Setter
public class ReverseBidding extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reverseBiddingNo; // 역경매식별자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member; // 회원

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_code")
    private Category category;

    @Column(nullable = false)
    private String reverseBiddingTitle; // 역경매제목

    @Column(nullable = false)
    private String reverseBiddingDetail; // 희망사항

    @Column(nullable = false)
    private Integer hopePrice; // 희망가

    @Column(nullable = false)
    private LocalDateTime reverseBiddingExpireDate; // 마감기간
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ReversebidAuctionStatus reversebidAuctionStatus; //경매상태
    
    @Column(columnDefinition = "integer default 0", nullable = false)	
    private Integer reverseBiddingCount; // 경매조회수
}
