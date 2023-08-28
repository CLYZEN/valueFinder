package com.ezen.valuefinder.dto;


import com.ezen.valuefinder.constant.AuctionStatus;
import com.ezen.valuefinder.constant.AuctionType;
import com.ezen.valuefinder.constant.BidStatus;
import com.ezen.valuefinder.entity.Item;
import com.ezen.valuefinder.entity.Member;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
public class MemberAuctionDto {
    private Long auctionNo;
    private Item item;
    private AuctionType auctionType;
    private Long auctionStartPrice;
    private Long auctionNowPrice;
    private LocalDateTime auctionStartTime;
    private LocalDateTime auctionEndTime;
    private AuctionStatus auctionStatus;
    private Integer auctionCount;
    private String remainingTime;
    private Integer biddingCount;
    private Long successBiddingNo;
    private Member member;
    private BidStatus bidStatus;
    private String shippingNo;

    public MemberAuctionDto(Long auctionNo, Item item, AuctionType auctionType, Long auctionStartPrice, Long auctionNowPrice, LocalDateTime auctionStartTime, LocalDateTime auctionEndTime, AuctionStatus auctionStatus, Integer auctionCount, String remainingTime, Integer biddingCount, Long successBiddingNo, Member member, BidStatus bidStatus, String shippingNo) {
        this.auctionNo = auctionNo;
        this.item = item;
        this.auctionType = auctionType;
        this.auctionStartPrice = auctionStartPrice;
        this.auctionNowPrice = auctionNowPrice;
        this.auctionStartTime = auctionStartTime;
        this.auctionEndTime = auctionEndTime;
        this.auctionStatus = auctionStatus;
        this.auctionCount = auctionCount;
        this.remainingTime = remainingTime;
        this.biddingCount = biddingCount;
        this.successBiddingNo = successBiddingNo;
        this.member = member;
        this.bidStatus = bidStatus;
        this.shippingNo = shippingNo;
    }
}
