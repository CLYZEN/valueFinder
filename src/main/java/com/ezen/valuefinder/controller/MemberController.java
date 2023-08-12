package com.ezen.valuefinder.controller;

import com.ezen.valuefinder.dto.MemberFormDto;
import com.ezen.valuefinder.entity.Bank;
import com.ezen.valuefinder.entity.Member;
import com.ezen.valuefinder.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {

	private final MemberService memberService;
	private final PasswordEncoder passwordEncoder;

	 @GetMapping(value = "/member/login")
	 public String login() {
	 	return "member/login";
	 }

	 @GetMapping(value = "/member/login/error")
	 public String loginError(Model model) {
		 model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인해주세요,");
		 return "member/login";
	 }

	 @GetMapping(value = "/member/regist")
	 public String register(Model model) {
		 List<Bank> bankList = memberService.getBankList();

		 model.addAttribute("bankList", bankList);
		 model.addAttribute("memberFormDto", new MemberFormDto());

		 return "member/register";
	 }

	 @PostMapping(value = "/member/regist")
	 public String register(@Valid MemberFormDto memberFormDto, BindingResult bindingResult, Model model) {
		List<Bank> bankList = memberService.getBankList();
		 if(bindingResult.hasErrors()) {
			 model.addAttribute("registChk","Check");
			 model.addAttribute("bankList",bankList);
			 model.addAttribute("memberFromDto", new MemberFormDto());
			 return "member/register";
		 }

		 try {
			 Member member = memberService.createMember(memberFormDto,passwordEncoder);
		 } catch (Exception e) {
			 model.addAttribute("registChk", "Check");
			 model.addAttribute("errorMessage", e.getMessage());
			 model.addAttribute("bankList",bankList);
			 model.addAttribute("memberFromDto", new MemberFormDto());
			 return "member/register";
		 }

		 return "member/login";
	 }

	 @GetMapping(value = "/member/findpw")
	 public String findPw() {
		 return "member/findpw";
	 }

	 @GetMapping(value = "member/regist/social")
	public String socialRegist() {
		 return "member/social";
	 }
<<<<<<< HEAD
	 
	 @GetMapping(value ="member/mypage") 
	 public String myPage() {
		 return "member/mypage";
	 }
}
=======
	}
>>>>>>> 09062c6f36e1940fd4476263589694fcf453f2c4
