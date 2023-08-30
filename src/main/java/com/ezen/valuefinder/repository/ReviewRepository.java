package com.ezen.valuefinder.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ezen.valuefinder.entity.AuctionReview;
import com.ezen.valuefinder.entity.Member;

public interface ReviewRepository extends JpaRepository<AuctionReview,Long>{
	Page<AuctionReview> findByAuctionItemMember(Member member, Pageable pageable);
	
	@Query("SELECT COUNT(i) FROM AuctionReview i left outer join auction a on i.auction.auctionNo = a.auctionNo left outer join item b on a.item.itemNo = b.itemNo left outer join Member c on b.member.memberId = c.memberId WHERE c.memberId = :memberId")
	int countAuctionReviewsByAuctionItemMember(@Param("memberId") Long memberId);
}
