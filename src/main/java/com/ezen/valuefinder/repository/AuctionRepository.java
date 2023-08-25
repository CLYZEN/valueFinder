package com.ezen.valuefinder.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ezen.valuefinder.constant.AuctionType;
import com.ezen.valuefinder.entity.Auction;
import com.ezen.valuefinder.entity.Category;
import com.ezen.valuefinder.entity.Member;

public interface AuctionRepository extends JpaRepository<Auction,Long> {
	Auction findByAuctionNo(Long auctionNo);

	Page<Auction> findByItemMember(Member member, Pageable pageable);

	Page<Auction> findByAuctionTypeOrderByAuctionEndTimeDescAuctionCountDesc(AuctionType auctionType, Pageable pageable);

	Page<Auction> findByAuctionTypeOrderByAuctionEndTimeDesc(AuctionType auctionType, Pageable pageable);

	Page<Auction> findByAuctionTypeAndItemCategoryOrderByAuctionEndTimeDesc(AuctionType auctionType, Pageable pageable, Category category);

}
