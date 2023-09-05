package com.ezen.valuefinder.controller;

import com.ezen.valuefinder.config.PrincipalDetails;
import com.ezen.valuefinder.constant.AuctionType;
import com.ezen.valuefinder.entity.Auction;
import com.ezen.valuefinder.service.AuctionService;
import com.ezen.valuefinder.service.BiddingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class BiddingController {

    private final BiddingService biddingService;
    private final AuctionService auctionService;

    @PostMapping(value = "/auction/bidding")
    public @ResponseBody ResponseEntity bidding(@RequestBody Map<String, Object> requestBody,
                                                Authentication authentication) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

        try {
            Long auctionNo = Long.parseLong(requestBody.get("auctionNo").toString());
            Long price = Long.parseLong(requestBody.get("price").toString());

            Auction auction = auctionService.getAuction(auctionNo);

            if(auction.getItem().getMember().getEmail().equals(principalDetails.getUsername())) {
                return new ResponseEntity("본인 경매에는 입찰할 수 없습니다.", HttpStatus.LOCKED);
            }

            switch (auction.getAuctionStatus()) {
                case PENDING -> {
                    return new ResponseEntity("경매 시작 전입니다.", HttpStatus.LOCKED);
                }
                case PROGRESS, LAST -> {

                    if(price <= 0) {
                        return new ResponseEntity("0보다 큰 숫자를 입력해주세요.", HttpStatus.BAD_REQUEST);
                    }

                    // 비공개 경매라면
                    if (auction.getAuctionType() == AuctionType.SEALED) {
                        if(biddingService.chkSealedBidding(auction,principalDetails.getMember())) {
                            biddingService.chkStatus(auction);
                            biddingService.joinBidding(principalDetails.getUsername(), auctionNo, price);
                            return new ResponseEntity("입찰 완료", HttpStatus.OK);
                        } else {
                            return new ResponseEntity("비공개경매는 한번만 입찰 가능합니다.", HttpStatus.BAD_REQUEST);
                        }
                    }

                    // 비공개 경매가 아니라면
                    if (auction.getAuctionType() != AuctionType.SEALED) {

                        if (price <= auction.getAuctionNowPrice()) {
                            return new ResponseEntity("현재 금액보다 큰 금액을 입력해주세요.", HttpStatus.BAD_REQUEST);
                        }

                        // 현재 최상위 입찰자인지 검사
                        if(biddingService.chkBidding(auction,principalDetails.getMember())) {
                            biddingService.chkStatus(auction);
                            biddingService.joinBidding(principalDetails.getUsername(), auctionNo, price);
                            return new ResponseEntity("입찰 완료", HttpStatus.OK);
                        } else {
                            return new ResponseEntity("현재 최상위 입찰자입니다.", HttpStatus.BAD_REQUEST);
                        }

                    }

                }
                case END -> {
                    return new ResponseEntity("종료된 경매입니다.", HttpStatus.LOCKED);
                }
            }
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(),HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }
}
