package com.ezen.valuefinder.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ezen.valuefinder.entity.Auction;

public interface AuctionRepository extends JpaRepository<Auction,Long> {
	Auction findByAuctionNo(Long auctionNo);
}
