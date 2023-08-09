package com.ezen.valuefinder.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class MemberController {
	
	//일반 회원가입 페이지
	@GetMapping(value = "member/register")
	public String memberRegister() {
		return "member/register";
	}
	
	
	
	//회원가입/카카오톡 회원가입연동 페이지
	@GetMapping(value = "member/register/social")
	public String memberkakao() {
		return "member/register/social";
	}
	
	@GetMapping(value = "member/findpw")
	public String findpw() {
		return "member/findpw";
	}
	
}
