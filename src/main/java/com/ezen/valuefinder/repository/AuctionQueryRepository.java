package com.ezen.valuefinder.repository;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ezen.valuefinder.entity.Auction;
import com.ezen.valuefinder.entity.AuctionQuery;
import com.ezen.valuefinder.entity.Item;
import com.ezen.valuefinder.entity.Member;



public interface AuctionQueryRepository extends JpaRepository<AuctionQuery,Long>{

	Page<AuctionQuery> findByMember(Pageable pageable , Member member );
	
	
}
