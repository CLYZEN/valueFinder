package com.ezen.valuefinder.repository;

import com.ezen.valuefinder.dto.MemberMyAuctionDto;
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

	@Query(value = "SELECT DISTINCT a.auction_no as auctionNo, a.auction_type as auctionType, a.auction_start_price as auctionStartPrice, a.auction_now_price as auctionNowPrice, a.bidding_count as biddingCount, a.auction_end_time as auctionEndTime, i.item_name as itemName, sb.bid_status as bidStatus, ii.item_image_url as itemImageUrl,sb.success_bidding_no as successBiddingNo" +
			"	FROM auction a" +
			"         INNER JOIN item i ON a.item_no = i.item_no" +
			"         INNER JOIN member m ON i.member_id = m.member_id" +
			"         LEFT JOIN success_bidding sb ON a.auction_no = sb.auction_no" +
			"         LEFT JOIN item_img ii ON i.item_no = ii.item_no" +
			"	WHERE i.member_id = :memberId" +
			"  AND ii.rep_image_yn = true",nativeQuery = true)
	Page<MemberMyAuctionDto> findAuctionsByMemberId(@Param("memberId") Long memberId, Pageable pageable);

	Page<Auction> findByAuctionNoIn(List<Long> auctionNo,Pageable pageable);
}
