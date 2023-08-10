package com.ezen.valuefinder.controller;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AuctionController {

	//실시간 경매 페이지
		@GetMapping(value="/realtime")
		public String auctionRealtime() {
			
			return "auction/realtime"; 
		}
		
	//비공개 경매 페이지
		@GetMapping(value="/sealedbid")
		public String auctionSealedbid() {
			
			return "auction/sealedbid"; 
		}
		
		
		//역경매 페이지
		@GetMapping(value="/reversebid")
		public String auctionReversebid() {
					
		return "auction/reversebid"; 
		}	
}
