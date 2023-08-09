package com.ezen.valuefinder.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuctionController {

	@GetMapping(value = "/item/add")
	public String addItem() {
		return "/auction/normalitemform";
	}
		
	@GetMapping(value = "/item/reverse/add")
	public String addReverseItem() {
		return "/auction/reverseitemform";
	}
}
