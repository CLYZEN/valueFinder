package com.ezen.valuefinder.entity;

import com.ezen.valuefinder.constant.AuctionStatus;
import com.ezen.valuefinder.constant.AuctionType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "auction")
@ToString
@Getter
@Setter
public class Auction extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long auctionNo; // 경매식별자

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_no")
    private Item item; // 물품

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AuctionType auctionType; // 경매구분

    @Column(nullable = false)
    private Long auctionStartPrice; // 경매시작가

    @Column(nullable = false)
    private Long auctionNowPrice; // 경매현재가

    @Column(nullable = false)
    private LocalDateTime auctionStartTime; // 경매시작일

    @Column(nullable = false)
    private LocalDateTime auctionEndTime; // 경매종료일

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AuctionStatus auctionStatus; // 경매상태

    @Column(columnDefinition = "integer default 0", nullable = false)	
    private Integer auctionCount; // 경매조회수
}
