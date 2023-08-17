package com.ezen.valuefinder.controller;

import com.ezen.valuefinder.dto.MemberFindPwDto;
import com.ezen.valuefinder.dto.MemberFormDto;
import com.ezen.valuefinder.dto.MemberModifyDto;
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

import java.security.Principal;
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
	 public String findPw(Model model) {
		 model.addAttribute("findPwDto", new MemberFindPwDto());

		 return "member/findpw";
	 }

	 @PostMapping(value = "/member/findpw")
	 public String findPw(@Valid MemberFindPwDto memberFindPwDto,Model model) {

		 if(memberService.findPwChkMember(memberFindPwDto) == null) {
			 model.addAttribute("findPwDto", new MemberFindPwDto());
			 model.addAttribute("errorMessage","일치하는 회원 정보가 없습니다.");
			 return "member/findpw";
		 }

		 model.addAttribute("email", memberFindPwDto.getEmail());
		 return "member/resetpw";
	 }

	 @PostMapping(value = "/member/updatepw")
	 public String updatePw(@Valid String password, @Valid String email) {
		memberService.updatePwd(password,email,passwordEncoder);

		 return "member/login";
	 }

	 @GetMapping(value = "member/regist/social")
	public String socialRegist() {
		 return "member/social";
	 }
	 
	 @GetMapping(value ="member/mypage") 
	 public String myPage(Model model,Principal principal) {
		Member member = memberService.findByEmail(principal.getName());
		model.addAttribute("member",member);
		 return "member/mypage";
	 }
	 
	 @GetMapping(value ="member/mypage/bidding")
	 public String bidding(Principal principal, Model model) {
		 Member member = memberService.findByEmail(principal.getName());
		 model.addAttribute("member",member);
		 return "member/bidding";
	 }
	 
	 @GetMapping(value ="member/mypage/successfulbid")
	 public String successfulbid(Principal principal, Model model) {
		 Member member = memberService.findByEmail(principal.getName());
		 model.addAttribute("member",member);
		 return "member/successfulbid";
	 }
	 
	 @GetMapping(value ="member/mypage/myauction")
	 public String myauction(Model model,Principal principal) {
		 Member member = memberService.findByEmail(principal.getName());
		 model.addAttribute("member",member);
		 return "member/myauction";
	 }
	 
	 @GetMapping(value ="member/mypage/modify/checkpwd")
	 public String checkpwd(Model model, Principal principal) {
		 Member member = memberService.findByEmail(principal.getName());
		 model.addAttribute("member",member);
		 return "member/checkpwd";
	 }

	 @PostMapping(value = "member/mypage/modify/checkpwd")
	 public String checkpwd(Model model, Principal principal,@Valid String password) {
		 boolean result = memberService.checkPwd(password,principal.getName(),passwordEncoder);
		 if(result == true) {
			 return "redirect:/member/mypage/modify";
		 } else {
			 model.addAttribute("errorMessage", "비밀번호가 일치하지 않습니다.");
			 return "member/checkpwd";
		 }
	 }

	 @GetMapping(value = "member/mypage/todayview")
	 public String todayViewAuction() {
		 return "member/todayviewauction";
	 }

	 @GetMapping(value ="member/mypage/modify/password")
	 public String changepwd(Model model, Principal principal) {
		 Member member = memberService.findByEmail(principal.getName());
		 model.addAttribute("member",member);
		 return "member/changepwd";
	 }
	 
	 @GetMapping(value ="member/mypage/outmember")
	 public String outmember(Model model, Principal principal) {
		 Member member = memberService.findByEmail(principal.getName());
		 model.addAttribute("member",member);
		 return "member/outmember";
	 }

	 @PostMapping(value = "member/mypage/outmember")
	 public String outmember(@Valid String detail,Principal principal) {
		memberService.memberOut(principal.getName(),detail);

		 return "redirect:/member/logout";
	 }
	 
	 @GetMapping(value ="member/mypage/coupon")
	 public String coupon(Model model,Principal principal) {
		 Member member = memberService.findByEmail(principal.getName());
		 model.addAttribute("member",member);
		 return "member/coupon";
	 }
	 
	 @GetMapping(value ="member/mypage/sentquery")
	 public String sentquery(Model model,Principal principal) {
		 Member member = memberService.findByEmail(principal.getName());
		 model.addAttribute("member",member);
		 return "member/sentquery";
	 }
	 
	 @GetMapping(value ="member/mypage/receivedquery")
	 public String receivedquery(Model model, Principal principal) {
		 Member member = memberService.findByEmail(principal.getName());
		 model.addAttribute("member",member);
		 return "member/receivedquery";
	 }
	 
	 @GetMapping(value ="member/mypage/like")
	 public String like(Model model, Principal principal) {
		 Member member = memberService.findByEmail(principal.getName());
		 model.addAttribute("member",member);
		 return "member/like";
	 }
	 
	 @GetMapping(value ="member/mypage/modify")
	 public String modify(Model model, Principal principal) {
		 List<Bank> bankList = memberService.getBankList();
		 Member member = memberService.findByEmail(principal.getName());

		 model.addAttribute("memberModifyDto",new MemberModifyDto());
		 model.addAttribute("member",member);
		 model.addAttribute("bankList", bankList);
		 return "member/modify";
	 }

	 // 에러메시지 구현 필요
	@PostMapping(value = "member/mypage/modify")
	 public String modify(@Valid MemberModifyDto memberModifyDto,BindingResult bindingResult,Model model,Principal principal) {
		 memberService.updateMember(memberModifyDto,principal.getName());
		 return "redirect:/";
	 }

	 //계정복구
	@GetMapping(value = "repair")
	public String repairMember() {
		 return "member/repairmember";
	}

	@PostMapping(value = "repair")
	public String repairMember(@Valid String email, @Valid String password,Model model) {
		 Member member = memberService.findByEmail(email);
		 boolean result = passwordEncoder.matches(password,member.getPassword());

		 if (member == null || result == false) {
			 model.addAttribute("errorMessage","아이디 또는 비밀번호가 틀렸습니다.");
			 return "member/repairmember";
		 }
		 memberService.repairMember(email);
		 return "redirect:/member/login";
	}
}
