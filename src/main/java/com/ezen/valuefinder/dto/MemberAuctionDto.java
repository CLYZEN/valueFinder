package com.ezen.valuefinder.dto;


import com.ezen.valuefinder.constant.AuctionStatus;
import com.ezen.valuefinder.constant.AuctionType;
import com.ezen.valuefinder.constant.BidStatus;
import com.ezen.valuefinder.entity.Item;
import com.ezen.valuefinder.entity.Member;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
public class MemberAuctionDto {
    private Long auctionNo;
    private Long auctionStartPrice;
    private Long auctionNowPrice;
    private Integer biddingCount;
    private LocalDateTime auctionEndTime;
    private String itemName;
    private String bidStatus;

    @Column(name = "auction_type")
    private String auctionType;
    private String itemImageUrl;
}
