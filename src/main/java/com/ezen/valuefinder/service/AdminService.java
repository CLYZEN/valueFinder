package com.ezen.valuefinder.service;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ezen.valuefinder.entity.Auction;
import com.ezen.valuefinder.entity.AuctionReport;
import com.ezen.valuefinder.entity.Member;
import com.ezen.valuefinder.repository.AuctionReportRepository;
import com.ezen.valuefinder.repository.AuctionRepository;
import com.ezen.valuefinder.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminService {
	private final MemberRepository memberRepository;
	private final AuctionRepository auctionRepository;
	private final AuctionReportRepository auctionReportRepository;
	
	public Member getMember(Long memberId) {
		Member member = memberRepository.findById(memberId).orElseThrow();
		
		return member;
	}
	
	public void deleteAuction(Long auctionNo) {
		Auction auction  = auctionRepository.findById(auctionNo).orElseThrow();
		
		auctionRepository.delete(auction);
	}
	
}
