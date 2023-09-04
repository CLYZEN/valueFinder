package com.ezen.valuefinder.service;

import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ezen.valuefinder.constant.BidStatus;
import com.ezen.valuefinder.constant.ReversebidAuctionStatus;
import com.ezen.valuefinder.entity.ReverseBidding;
import com.ezen.valuefinder.entity.ReverseBiddingSuccess;
import com.ezen.valuefinder.repository.MemberRepository;
import com.ezen.valuefinder.repository.ReverseBiddingRepository;
import com.ezen.valuefinder.repository.ReversebidSuccessRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
@EnableAsync
public class ReversebidSuccessServiceImpl implements ReversebidSuccessService{
	private final ReversebidSuccessRepository reversebidSuccessRepository;
	private final MemberRepository memberRepository;
	private final ReverseBiddingRepository reverseBiddingRepository;
	
	@Override
	public void addReverseBidding(Long reverseBiddingNo, Long memberId) {
		ReverseBidding reverseBidding = reverseBiddingRepository.findById(reverseBiddingNo).orElseThrow();
		reverseBidding.setReversebidAuctionStatus(ReversebidAuctionStatus.END);
		reverseBiddingRepository.save(reverseBidding);
		
		
		ReverseBiddingSuccess reverseBiddingSuccess = new ReverseBiddingSuccess();
		reverseBiddingSuccess.setReverseBidding(reverseBiddingRepository.findById(reverseBiddingNo).orElseThrow());
		reverseBiddingSuccess.setMember(memberRepository.findById(memberId).orElseThrow());
		reverseBiddingSuccess.setBidStatus(BidStatus.PENDING);
		reversebidSuccessRepository.save(reverseBiddingSuccess);
	}
}
