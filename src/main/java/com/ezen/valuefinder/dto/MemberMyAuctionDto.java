package com.ezen.valuefinder.dto;

import java.time.LocalDateTime;

public interface MemberMyAuctionDto {
    Long getAuctionNo();
    Long getAuctionStartPrice();
    Long getAuctionNowPrice();
    Integer getBiddingCount();
    LocalDateTime getAuctionEndTime();
    String getItemName();
    String getBidStatus();
    String getAuctionType();
    String getItemImageUrl();
    Long getSuccessBiddingNo();
}
