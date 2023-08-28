package com.ezen.valuefinder.service;

import com.ezen.valuefinder.constant.AuctionStatus;
import com.ezen.valuefinder.constant.AuctionType;
import com.ezen.valuefinder.constant.BidStatus;
import com.ezen.valuefinder.entity.Auction;
import com.ezen.valuefinder.entity.Bidding;
import com.ezen.valuefinder.entity.Member;
import com.ezen.valuefinder.entity.SuccessBidding;
import com.ezen.valuefinder.repository.AuctionRepository;
import com.ezen.valuefinder.repository.BiddingRepository;
import com.ezen.valuefinder.repository.SuccessBiddingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class BiddingService {

    private final BiddingRepository biddingRepository;
    private final MemberService memberService;
    private final AuctionRepository auctionRepository;
    private final SuccessBiddingRepository successBiddingRepository;

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
        Bidding bidding = biddingRepository.findTopByAuctionOrderByBiddingPriceDesc(auction);
        if (bidding != null) {
            if(bidding.getMember() == member) {
                return false;
            }
        } else if (bidding == null){
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
        successBidding.setShippingNo(data);
    }


}
