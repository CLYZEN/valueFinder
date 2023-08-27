package com.ezen.valuefinder.service;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ezen.valuefinder.entity.Member;
import com.ezen.valuefinder.repository.AuctionRepository;
import com.ezen.valuefinder.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminService {
	private final MemberRepository memberRepository;
	private final AuctionRepository auctionRepository;
	
	public Member getMember(Long memberId) {
		Member member = memberRepository.findById(memberId).orElseThrow();
		
		return member;
	}
	
}
