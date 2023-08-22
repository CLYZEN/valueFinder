package com.ezen.valuefinder.repository;

import com.ezen.valuefinder.entity.Auction;
import com.ezen.valuefinder.entity.Member;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AuctionRepository extends JpaRepository<Auction,Long> {
	
	@Modifying
	@Query("update Auction a set a.auctionCount = a.auctionCount+1 where a.id = :id")
	int auctionCount(Long id);
	
	Page<Auction> findByItemMember(Member member, Pageable pageable);
	
}
