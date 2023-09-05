package com.ezen.valuefinder.repository;

import com.ezen.valuefinder.constant.AuctionStatus;
import com.ezen.valuefinder.constant.AuctionType;
import com.ezen.valuefinder.dto.BiddingCountsDTO;
import com.ezen.valuefinder.entity.Auction;
import com.ezen.valuefinder.entity.Bidding;
import com.ezen.valuefinder.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BiddingRepository extends JpaRepository<Bidding,Long> {
    Bidding findTopByAuctionOrderByBiddingPriceDesc(Auction auction);
    Integer countByAuction(Auction auction);
    Bidding findByAuctionAndMember(Auction auction, Member member);
    Bidding findTopByAuctionAndMemberNotInOrderByBiddingPriceDesc(Auction auction, List<Member> memberList);
    
    List<Bidding> findByAuctionOrderByBiddingPriceDesc(Auction auction);

    @Query("SELECT COUNT(b) " +
            "FROM Bidding b " +
            "WHERE b.auction.auctionType = :auctionType " +
            "AND b.auction.auctionStatus != 'Pending' " +
            "AND b.auction.auctionStatus != 'End' " +
            "AND b.member = :member ")
    Long getBiddingInProgressCount(@Param("member") Member member, @Param("auctionType")AuctionType auctionType);

    @Query("SELECT b FROM Bidding b " +
            "WHERE b.auction.auctionType = :auctionType " +
            "AND b.auction.auctionStatus != 'Pending' " +
            "AND b.auction.auctionStatus != 'End' " +
            "AND b.member = :member")
    List<Bidding> getBiddingInProgress(@Param("member") Member member, @Param("auctionType") AuctionType auctionType);

    @Query("SELECT COUNT(b) " +
            "FROM Bidding b " +
            "WHERE b.auction.auctionType = :auctionType " +
            "AND b.member = :member " +
            "AND b.auction.auctionStatus != 'Pending' " +
            "AND b.auction.auctionStatus != 'End' " +
            "AND b.biddingPrice = (SELECT MAX(b2.biddingPrice) FROM Bidding b2 " +
            "WHERE b2.auction = b.auction)")
    Long getMaxBiddingCountForMemberAndAuctionStatus(@Param("member") Member member, @Param("auctionType")AuctionType auctionType);

    @Query("SELECT b FROM Bidding b " +
            "WHERE b.auction.auctionType = :auctionType " +
            "AND b.auction.auctionStatus != 'Pending' " +
            "AND b.auction.auctionStatus != 'End' " +
            "AND b.member = :member " +
            "AND b.biddingPrice = (SELECT MAX(b2.biddingPrice) FROM Bidding b2 " +
            "WHERE b2.auction = b.auction)")
    List<Bidding> getMaxBiddingsForMemberAndAuctionStatus(@Param("member") Member member, @Param("auctionType") AuctionType auctionType);


}
