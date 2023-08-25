package com.ezen.valuefinder.repository;

import com.ezen.valuefinder.entity.Auction;
import com.ezen.valuefinder.entity.Bidding;
import com.ezen.valuefinder.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BiddingRepository extends JpaRepository<Bidding,Long> {
    Bidding findTopByAuctionOrderByBiddingPriceDesc(Auction auction);
    Integer countByAuction(Auction auction);

    Bidding findByAuctionAndMember(Auction auction, Member member);
}
