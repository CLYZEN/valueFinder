package com.ezen.valuefinder.repository;

import com.ezen.valuefinder.entity.Auction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface AuctionRepository extends JpaRepository<Auction,Long> {
	
	@Modifying
	@Query("update Auction a set a.auctionCount = a.auctionCount+1 where a.id = :id")
	int auctionCount(Long id);
}
