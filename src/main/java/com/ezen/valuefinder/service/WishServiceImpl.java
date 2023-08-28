package com.ezen.valuefinder.service;

import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ezen.valuefinder.entity.Wish;
import com.ezen.valuefinder.repository.AuctionRepository;
import com.ezen.valuefinder.repository.MemberRepository;
import com.ezen.valuefinder.repository.WishRepository;


import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
@EnableAsync
public class WishServiceImpl implements WishService{
	
	 private final WishRepository wishRepository;
	 private final MemberRepository memberRepository;
	 private final AuctionRepository auctionRepository;
	
	@Override
	public void addWish(Long auctionNo, Long memberId) {
		Wish wish = new Wish();
		wish.setAuction(auctionRepository.findById(auctionNo).orElseThrow());
		wish.setMember(memberRepository.findById(memberId).orElseThrow());
	     wishRepository.save(wish);
		
	}
	@Override
	public void deleteWish(Long auctionNo, Long memberId) {
		Wish wish = wishRepository.findByAuctionAuctionNoAndMemberMemberId(auctionNo, memberId);
	       wishRepository.delete(wish);
		
	}
	
	@Override
	public boolean checkWish(Long auctionNo, Long memberId) {
		
		return wishRepository.existsByAuctionAuctionNoAndMemberMemberId(auctionNo, memberId);
	}
		
}
