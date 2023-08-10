package com.ezen.valuefinder.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MemberController {

	 @GetMapping(value = "/member/login2")
	 public String login() {
	 	return "member/login2";
	 }

	 @GetMapping(value = "/member/regist")
	 public String register() {
		 return "member/register";
	 }

	 @GetMapping(value = "/member/findpw")
	 public String findPw() {
		 return "member/findpw";
	 }

	 @GetMapping(value = "member/regist/social")
	public String socialRegist() {
		 return "member/social";
	 }
	 
	 @GetMapping(value ="member/mypage") 
	 public String myPage() {
		 return "member/mypage";
	 }
}
