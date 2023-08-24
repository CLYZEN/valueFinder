package com.ezen.valuefinder.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.ezen.valuefinder.config.PrincipalDetails;
import com.ezen.valuefinder.dto.WishDto;
import com.ezen.valuefinder.service.WishService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class WishController {
	
	private final WishService wishService;
	 
	
	 //경매 글에 찜 추가

	 @PostMapping("/addWish") 
	 public void addLike(@RequestBody WishDto wishDto,Authentication authentication) { 
		 PrincipalDetails principalDetails =(PrincipalDetails) authentication.getPrincipal(); //사용자 정보 가져옴.
	 wishService.addWish(wishDto.getAuctionNo(), principalDetails.getMember().getMemberId()); }
	 
	 //경매 글에 찜 취소
	 
	 @DeleteMapping("/deleteWish")
	 public void deleteLike(@RequestBody WishDto wishDto,Authentication authentication) { 
		 PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
		 wishService.deleteWish(wishDto.getAuctionNo(), principalDetails.getMember().getMemberId() );}
	 
	
	@PostMapping("/checkWish")
    public ResponseEntity<String> checkWish(@RequestBody WishDto wishDto, Authentication authentication) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        Long memberId = principalDetails.getMember().getMemberId();
        Long auctionNo = wishDto.getAuctionNo();
        
        if (!wishService.checkWish(auctionNo, memberId)) {
            wishService.addWish(auctionNo, memberId);
        } else {
            wishService.deleteWish(auctionNo, memberId);
        }
        
        return ResponseEntity.ok("좋아요 완료.");
    }

	 
}
	 

