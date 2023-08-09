package com.ezen.valuefinder.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MyPageController {
	
	@GetMapping(value = "/mypage")
	public String test3() {
		return "mypage/mypage";
	}
	
}
