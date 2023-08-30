package com.ezen.valuefinder.repository;

import com.ezen.valuefinder.entity.Auction;
import com.ezen.valuefinder.entity.Bidding;
import com.ezen.valuefinder.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BiddingRepository extends JpaRepository<Bidding,Long> {
    Bidding findTopByAuctionOrderByBiddingPriceDesc(Auction auction);
    Integer countByAuction(Auction auction);
    Bidding findByAuctionAndMember(Auction auction, Member member);
    Bidding findTopByAuctionAndMemberNotInOrderByBiddingPriceDesc(Auction auction, List<Member> memberList);
}
