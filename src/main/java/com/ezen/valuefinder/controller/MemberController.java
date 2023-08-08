package com.ezen.valuefinder.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class MemberController {

	@GetMapping(value = "member/register")
	public String memberRegister() {
		return "member/register";
	}
	
	
	@GetMapping(value = "member/register/kakao")
	public String memberkakao() {
		return "member/register/social";
	}
	
}
