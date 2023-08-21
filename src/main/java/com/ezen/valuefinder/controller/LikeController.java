package com.ezen.valuefinder.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.ezen.valuefinder.dto.LikeRequestDto;
import com.ezen.valuefinder.service.LikeService;


public class LikeController {
	 private final LikeService likeService;
	 
	// 경매 글에 찜 추가
	    @PostMapping("/addLike")
	    public void addLike(@RequestBody LikeRequestDto likeRequest) {
	        likeService.addLike(likeRequest.getAuctionNo(), likeRequest.getMemberId());
	    }

	 // 경매 글에 찜 취소
	    @DeleteMapping("/deleteLike")
	    public void deleteLike(@RequestBody LikeRequestDto likeRequest) {
	        likeService.deleteLike(likeRequest.getAuctionNo(), likeRequest.getMemberId());
	    }

	}
	 
}
