package com.ezen.valuefinder.repository;

import java.util.concurrent.CompletableFuture;

import com.ezen.valuefinder.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ezen.valuefinder.entity.Wish;

public interface WishRepository extends JpaRepository<Wish,Long>{
    Wish findByAuctionAuctionNoAndMemberMemberId(Long auctionNo, Long memberId);
    boolean existsByAuctionAuctionNoAndMemberMemberId(Long auctionNo, Long memberId); //참거짓 분별
    CompletableFuture<Long> countByAuctionAuctionNo(Long auctionNo); //이미 체크 되어있는지 아닌지 확인

    Page<Wish> findByMember(Member member, Pageable pageable);
}
