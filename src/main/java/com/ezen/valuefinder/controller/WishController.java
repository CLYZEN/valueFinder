package com.ezen.valuefinder.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.ezen.valuefinder.dto.WishDto;
import com.ezen.valuefinder.service.WishService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class WishController {
	
	private final WishService wishService;
	 
	//경매 글에 찜 추가
	@PostMapping("/addWish")
	public void addLike(@RequestBody WishDto wishDto) {
	wishService.addWish(wishDto.getAuctionNo(), wishDto.getMemberId());
	}

	 //경매 글에 찜 취소
	 @DeleteMapping("/deleteWish")
	 public void deleteLike(@RequestBody WishDto wishDto) {
	 wishService.deleteWish(wishDto.getAuctionNo(), wishDto.getMemberId());
	}

		}
	 

