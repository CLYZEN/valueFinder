package com.ezen.valuefinder.service;

import com.ezen.valuefinder.constant.AuctionStatus;
import com.ezen.valuefinder.entity.Auction;
import com.ezen.valuefinder.entity.Bidding;
import com.ezen.valuefinder.repository.AuctionRepository;
import com.ezen.valuefinder.repository.BiddingRepository;
import lombok.RequiredArgsConstructor;
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

    public void joinBidding(String email, Long auctionNo, Long price) {
        Auction auction = auctionRepository.findById(auctionNo).orElseThrow();

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
}
