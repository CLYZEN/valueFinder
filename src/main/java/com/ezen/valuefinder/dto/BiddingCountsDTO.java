package com.ezen.valuefinder.dto;

import com.ezen.valuefinder.entity.Bidding;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BiddingCountsDTO {
        private Long countBiddingInProgress;
        private Long countTopBidder;
        private List<Bidding> biddingInProgress;
        private List<Bidding> topBidder;
    }

