package com.ezen.valuefinder.repository;

import com.ezen.valuefinder.entity.Member;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ezen.valuefinder.entity.Auction;
import com.ezen.valuefinder.entity.AuctionQuery;



public interface AuctionQueryRepository extends JpaRepository<AuctionQuery,Long>{
    Page<AuctionQuery> findByMember(Pageable pageable , Member member);

    Page<AuctionQuery> findByAuctionItemMember(Pageable pageable, Member member);
    
    List<AuctionQuery> findByAuction(Auction auction);
}
