package com.ezen.valuefinder.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ezen.valuefinder.config.PrincipalDetails;
import com.ezen.valuefinder.dto.WishDto;
import com.ezen.valuefinder.service.WishService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class WishController {

	private final WishService wishService;

	// 경매 글에 찜 추가

	@PostMapping("/addWish")
	public @ResponseBody ResponseEntity<String> addLike(@RequestBody WishDto wishDto, Authentication authentication) {
		PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal(); // 사용자 정보 가져옴.
		Long memberId = principalDetails.getMember().getMemberId();
		
		try {
			if (wishService.checkWish(wishDto.getAuctionNo(), memberId)) {
				
				System.out.println("이미 찜이 되어있음");
			} else { //체크가 되어있지 않으면 체크
				wishService.addWish(wishDto.getAuctionNo(), memberId);	        
			}			
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(),HttpStatus.BAD_REQUEST);
		}
		
		return new ResponseEntity<String>("success", HttpStatus.OK); //성공시
	}

	// 경매 글에 찜 취소

	@DeleteMapping("/deleteWish")
	public void deleteLike(@RequestBody WishDto wishDto, Authentication authentication) {
		PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
		wishService.deleteWish(wishDto.getAuctionNo(), principalDetails.getMember().getMemberId());
	}

}
