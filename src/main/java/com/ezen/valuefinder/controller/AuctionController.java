package com.ezen.valuefinder.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuctionController {

	@GetMapping(value = "/auction/add")
	public String addItem() {
		return "/auction/form/normalitemform";
	}
		
	@GetMapping(value = "/auction/reverse/add")
	public String addReverseItem() {
		return "/auction/form/reverseitemform";
	}

	@GetMapping(value = "/auction/public/detail")
	public String publicBidDetail() {
		return "/auction/details/publicdetail";
	}

	@GetMapping(value = "/auction/query/add")
	public String auctionQuery() {
		return "/auction/query/queryform";
	}

	@GetMapping(value = "/auction/query")
	public String queryDetail() {
		return "/auction/query/query";
	}

	@GetMapping(value = "/auction/report")
	public String reportAuction() {
		return "/auction/report";
	}

	@GetMapping(value = "/auction/reversebid/enter/add")
	public String enterQuery() {
		return "/auction/enter/enterForm";
	}
	
	@GetMapping(value = "/auction/reversebid/enter")
	public String enterDetail() {
		return "/auction/enter/enter";
	}


	//실시간 경매 페이지
	@GetMapping(value="/auction/realtime")
	public String auctionRealtime() {

		return "auction/realtime";
	}

	//비공개 경매 페이지
	@GetMapping(value="/auction/sealedbid")
	public String auctionSealedbid() {

		return "auction/sealedbid";
	}

}
