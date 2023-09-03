package com.ezen.valuefinder.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.ezen.valuefinder.constant.AuctionStatus;
import com.ezen.valuefinder.constant.AuctionType;
import com.ezen.valuefinder.dto.MemberAuctionDto;
import com.ezen.valuefinder.entity.Auction;
import com.ezen.valuefinder.entity.Category;
import com.ezen.valuefinder.entity.Member;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AuctionRepository extends JpaRepository<Auction,Long> {

	Auction findByAuctionNo(Long auctionNo);

	Page<Auction> findByItemMemberOrderByAuctionEndTimeDesc(Member member,Pageable pageable);

	Page<Auction> findByAuctionTypeOrderByAuctionEndTimeDescAuctionCountDesc(AuctionType auctionType, Pageable pageable);

	Page<Auction> findByAuctionTypeOrderByAuctionEndTimeDesc(AuctionType auctionType, Pageable pageable);

	Page<Auction> findByAuctionTypeAndItemCategoryOrderByAuctionEndTimeDesc(AuctionType auctionType, Pageable pageable, Category category);

	Page<Auction> findByItemCategoryOrderByAuctionEndTimeDesc(Category category, Pageable pageable);
	Page<Auction> findAllByOrderByAuctionEndTimeDescAuctionCountDesc(Pageable pageable);
	@Query("SELECT a FROM Auction a " +
			"WHERE a.auctionEndTime > CURRENT_TIMESTAMP " +
			"AND a.auctionStatus <> :status " +
			"ORDER BY (a.auctionEndTime - CURRENT_TIMESTAMP) ASC")
	Page<Auction> findActiveAuctionsOrderByTimeLeft(Pageable pageable, AuctionStatus status);
	Page<Auction> findByItemItemNameContainingAndItemCategoryOrderByAuctionEndTimeDesc(Pageable pageable, String name, Category category);
	Page<Auction> findByItemItemNameContainingOrderByAuctionEndTimeDesc(Pageable pageable, String name);

	Page<Auction> findAllByOrderByRegTime(Pageable pageable);

	@Query("SELECT new com.ezen.valuefinder.dto.MemberAuctionDto(a.auctionNo, a.item, a.auctionType, a.auctionStartPrice, a.auctionNowPrice, a.auctionStartTime, a.auctionEndTime, a.auctionStatus, a.auctionCount, a.remainingTime, a.biddingCount, sb.successBiddingNo, sb.member, sb.bidStatus, sb.shippingNo) " +
			"FROM Auction a LEFT JOIN FETCH SuccessBidding sb ON a = sb.auction WHERE sb.member.memberId = :memberId")
	Page<MemberAuctionDto> findAuctionsByMemberId(@Param("memberId") Long memberId, Pageable pageable);

}
