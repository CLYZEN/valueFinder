package com.ezen.valuefinder.repository;

import java.util.concurrent.CompletableFuture;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ezen.valuefinder.entity.Wish;

public interface WishRepository extends JpaRepository<Wish,Long>{
    Wish findByAuctionAuctionNoAndMemberMemberId(Long auctionNo, Long memberId);
    boolean existsByAuctionAuctionNoAndMemberMemberId(Long auctionNo, Long memberId);
    CompletableFuture<Long> countByAuctionAuctionNo(Long auctionNo);
}
