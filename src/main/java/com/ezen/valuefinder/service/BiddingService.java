package com.ezen.valuefinder.service;

import com.ezen.valuefinder.constant.AuctionStatus;
import com.ezen.valuefinder.constant.AuctionType;
import com.ezen.valuefinder.constant.BidStatus;
import com.ezen.valuefinder.dto.BiddingCountsDTO;
import com.ezen.valuefinder.entity.Auction;
import com.ezen.valuefinder.entity.Bidding;
import com.ezen.valuefinder.entity.Member;
import com.ezen.valuefinder.entity.SuccessBidding;
import com.ezen.valuefinder.repository.AuctionRepository;
import com.ezen.valuefinder.repository.BiddingRepository;
import com.ezen.valuefinder.repository.MemberRepository;
import com.ezen.valuefinder.repository.SuccessBiddingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BiddingService {

    private final BiddingRepository biddingRepository;
    private final MemberService memberService;
    private final AuctionRepository auctionRepository;
    private final SuccessBiddingRepository successBiddingRepository;
    private final MemberRepository memberRepository;

    public void joinBidding(String email, Long auctionNo, Long price) {
        Auction auction = auctionRepository.findById(auctionNo).orElseThrow();

        if(auction.getAuctionType() == AuctionType.REALTIME) {
            if(auction.getAuctionStatus() == AuctionStatus.LAST) {
                auction.setAuctionEndTime(auction.getAuctionEndTime().plusMinutes(1));
            }
        }

        Bidding bidding = new Bidding();
        bidding.setBiddingPrice(price);
        bidding.setBiddingTime(LocalDateTime.now());
        bidding.setAuction(auction);
        bidding.setMember(memberService.findByEmail(email));
        bidding.setBidStatus(BidStatus.PENDING);

        auction.setBiddingCount(auction.getBiddingCount()+1);
        auction.setAuctionNowPrice(price);

        biddingRepository.save(bidding);
    }

    public void chkStatus(Auction auction) {
        if (auction.getAuctionStatus().equals(AuctionStatus.LAST)) {
            auction.setAuctionStatus(AuctionStatus.PROGRESS);
            auction.setAuctionEndTime(auction.getAuctionEndTime().plusMinutes(1));
        }
    }

    public boolean chkBidding(Auction auction, Member member) {
        try {
            Bidding bidding = biddingRepository.findTopByAuctionOrderByBiddingPriceDesc(auction);
            Member maxBidMember = bidding.getMember();
            if (bidding != null) {
                if(maxBidMember.getEmail().equals(member.getEmail())) {
                    return false;
                } else {
                    return true;
                }
            }
        }catch (Exception e) {
            return true;
        }

       return false;
    }

    public boolean chkSealedBidding(Auction auction, Member member) {
        Bidding bidding = biddingRepository.findByAuctionAndMember(auction,member);

        if (bidding == null) {
            return true;
        }
        return false;
    }

    public Page<SuccessBidding> getMemberSuccessBiddingList(Member member, Pageable pageable) {
        return successBiddingRepository.findByMember(member,pageable);
    }

    public void updateBidStatus(Long id, String status) {
        SuccessBidding successBidding = successBiddingRepository.findById(id).orElseThrow();
        if(status.equals("NO")) {
            successBidding.setBidStatus(BidStatus.NO);

            Auction auction = successBidding.getAuction();
            List<Member> memberList = new ArrayList<>();
            List<SuccessBidding> successBiddingList = successBiddingRepository.findByBidStatusAndAuction(BidStatus.NO,auction);
            for(SuccessBidding successBidding2 : successBiddingList) {
                memberList.add(successBidding2.getMember());
            }
            Bidding bidding = biddingRepository.findTopByAuctionAndMemberNotInOrderByBiddingPriceDesc(auction,memberList);

            if(bidding == null) {
                return;
            }

            SuccessBidding newSuccessBidding = new SuccessBidding();
            newSuccessBidding.setBidStatus(BidStatus.PENDING);
            newSuccessBidding.setAuction(auction);
            newSuccessBidding.setMember(bidding.getMember());
            successBiddingRepository.save(newSuccessBidding);

        } else if (status.equals("OK")) {
            successBidding.setBidStatus(BidStatus.OK);
        } else if (status.equals("CONFIRM")) {
            successBidding.setBidStatus(BidStatus.CONFIRM);
        } else if (status.equals("SHIPPING")) {
            successBidding.setBidStatus(BidStatus.SHIPPING);
        } else if (status.equals("END")) {
            successBidding.setBidStatus(BidStatus.END);
        }
    }
    public void setShippingNo(Long id, String data) {
        SuccessBidding successBidding =successBiddingRepository.findById(id).orElseThrow();
        successBidding.updateShippingNo(data);
        successBiddingRepository.save(successBidding);
    }

    public List<Bidding>getBiddingList(Pageable pageable, Auction auction){
    	return biddingRepository.findByAuctionOrderByBiddingPriceDesc(auction);
    }

    public BiddingCountsDTO getMypageCount(AuctionType auctionType,Member member) {
        BiddingCountsDTO biddingCountsDTO = new BiddingCountsDTO();
        Long maxBiddingCount = biddingRepository.getMaxBiddingCountForMemberAndAuctionStatus(member,auctionType);
        Long biddingCount = biddingRepository.getBiddingInProgressCount(member,auctionType);
        List<Bidding> biddingList = biddingRepository.getBiddingInProgress(member,auctionType);
        List<Bidding> maxBiddingList = biddingRepository.getMaxBiddingsForMemberAndAuctionStatus(member,auctionType);

        biddingCountsDTO.setCountBiddingInProgress(biddingCount);
        biddingCountsDTO.setCountTopBidder(maxBiddingCount);
        biddingCountsDTO.setBiddingInProgress(biddingList);
        biddingCountsDTO.setTopBidder(maxBiddingList);

        return biddingCountsDTO;
    }
}
