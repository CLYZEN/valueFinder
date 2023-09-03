package com.ezen.valuefinder.controller;

import com.ezen.valuefinder.config.PrincipalDetails;
import com.ezen.valuefinder.constant.BidStatus;
import com.ezen.valuefinder.dto.*;
import com.ezen.valuefinder.entity.*;
import com.ezen.valuefinder.repository.BiddingRepository;
import com.ezen.valuefinder.service.AuctionQueryService;
import com.ezen.valuefinder.service.AuctionService;
import com.ezen.valuefinder.service.BiddingService;
import com.ezen.valuefinder.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;


import java.io.Console;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class MemberController {

	private final MemberService memberService;
	private final PasswordEncoder passwordEncoder;
	private final AuctionService auctionService;
	private final BiddingService biddingService;
	private final AuctionQueryService auctionQueryService;
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
	 public String myPage(Model model, Authentication authentication) {
		 PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
		 Member member = principalDetails.getMember();
		model.addAttribute("member",member);
		 return "member/mypage";
	 }
	 
	 @GetMapping(value ="member/mypage/bidding")
	 public String bidding(Authentication authentication, Model model) {
		 PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
		 Member member = principalDetails.getMember();
		 model.addAttribute("member",member);
		 return "member/bidding";
	 }
	 
	 @GetMapping(value ={"member/mypage/successfulbid","member/mypage/successfulbid/{page}"})
	 public String successfulbid(Authentication authentication, Model model,@PathVariable("page") Optional<Integer> page) {
		 PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
		 Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 4);
		 Member member = principalDetails.getMember();
		 Page<SuccessBidding> successBiddingList = biddingService.getMemberSuccessBiddingList(member,pageable);
		 model.addAttribute("reviewFormDto", new ReviewFormDto());
		 model.addAttribute("successBiddingList",successBiddingList);
		 model.addAttribute("maxPage",5);
		 model.addAttribute("member",member);
		 return "member/successfulbid";
	 }
	 
	 @GetMapping(value ={"member/mypage/myauction","member/mypage/myauction/{page}"})
	 public String myauction(Model model,Authentication authentication,@PathVariable("page") Optional<Integer> page) {
		 PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
		 Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 4);
		 Member member = principalDetails.getMember();
		 Page<MemberAuctionDto> auctionList = auctionService.getMemberAuctionList(member.getMemberId(),pageable);
		 model.addAttribute("auctionList",auctionList);
		 model.addAttribute("maxPage", 5);
		 model.addAttribute("member",member);
		 return "member/myauction";
	 }

	 
	 @GetMapping(value ="member/mypage/modify/checkpwd")
	 public String checkpwd(Model model, Authentication authentication) {
		 PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
		 Member member = principalDetails.getMember();
		 model.addAttribute("member",member);
		 return "member/checkpwd";
	 }

	 @PostMapping(value = "member/mypage/modify/checkpwd")
	 public String checkpwd(Model model, Authentication authentication,@Valid String password) {
		 PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();


		 boolean result = memberService.checkPwd(password,principalDetails.getUsername(),passwordEncoder);
		 if(result == true) {
			 return "redirect:/member/mypage/modify";
		 } else {
			 model.addAttribute("member",principalDetails.getMember());
			 model.addAttribute("errorMessage", "비밀번호가 일치하지 않습니다.");
			 return "member/checkpwd";
		 }
	 }

	 @GetMapping(value = "member/mypage/todayview")
	 public String todayViewAuction() {
		 return "member/todayviewauction";
	 }

	 @GetMapping(value ="member/mypage/modify/password")
	 public String changepwd(Model model, Authentication authentication) {
		 PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
		 Member member = principalDetails.getMember();
		 model.addAttribute("member",member);
		 return "member/changepwd";
	 }
	 
	 @GetMapping(value ="member/mypage/outmember")
	 public String outmember(Model model, Authentication authentication) {
		 PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
		 Member member = principalDetails.getMember();
		 model.addAttribute("member",member);
		 return "member/outmember";
	 }

	 @PostMapping(value = "member/mypage/outmember")
	 public String outmember(@Valid String detail,Authentication authentication) {
		 PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
		memberService.memberOut(principalDetails.getUsername(),detail);

		 return "redirect:/member/logout";
	 }
	 
	 @GetMapping(value ="member/mypage/coupon")
	 public String coupon(Model model,Authentication authentication) {
		 PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
		 Member member = principalDetails.getMember();
		 model.addAttribute("member",member);
		 return "member/coupon";
	 }
	 
	 @GetMapping(value ={"member/mypage/sentquery","member/mypage/sentquery/{page}"})
	 public String sentquery(Model model,Authentication authentication,@PathVariable("page") Optional<Integer> page) {
		 PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
		 Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 5);
		 Page<AuctionQuery> auctionQueryList = auctionQueryService.getAuctionQueryPage(pageable,principalDetails.getMember());
		 Member member = principalDetails.getMember();
		 model.addAttribute("auctionQueryList",auctionQueryList);
		 model.addAttribute("member",member);
		 model.addAttribute("maxPage",5);
		 return "member/sentquery";
	 }
	 

	 
	 
	 @GetMapping(value ={"member/mypage/receivedquery","member/mypage/receivedquery/{page}"})
	 public String receivedquery(Model model, Authentication authentication,@PathVariable("page") Optional<Integer> page) {
		 PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
		 Member member = principalDetails.getMember();
		 Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 5);
		Page<AuctionQuery> auctionQueryList = auctionQueryService.getReceiveQueryPage(pageable,member);

		model.addAttribute("auctionQueryList", auctionQueryList);
		 model.addAttribute("member",member);
		 model.addAttribute("maxPage",5);
		 return "member/receivedquery";
	 }
	 
	 @GetMapping(value ={"member/mypage/like","member/mypage/like/{page}"})
	 public String like(Model model, Authentication authentication,@PathVariable("page") Optional<Integer> page) {
		 PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
		 Member member = principalDetails.getMember();
		 Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 5);

		 Page<Wish> wishList = memberService.getMemberWishList(member,pageable);
		 model.addAttribute("wishList", wishList);
		 model.addAttribute("maxPage",5);
		 model.addAttribute("member",member);
		 return "member/like";
	 }
	 
	 @GetMapping(value ="member/mypage/modify")
	 public String modify(Model model, Authentication authentication) {
		 List<Bank> bankList = memberService.getBankList();
		 PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
		 Member member = principalDetails.getMember();

		 model.addAttribute("memberModifyDto",new MemberModifyDto());
		 model.addAttribute("member",member);
		 model.addAttribute("bankList", bankList);
		 return "member/modify";
	 }

	 // 에러메시지 구현 필요
	@PostMapping(value = "member/mypage/modify")
	 public String modify(@Valid MemberModifyDto memberModifyDto,BindingResult bindingResult,Model model,Authentication authentication) {
		PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

		 memberService.updateMember(memberModifyDto,principalDetails.getUsername());
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

	@PostMapping(value = "/member/shipping/{id}")
	public String updateBidStatus(@PathVariable Long id, Authentication authentication,@Valid String data) {
		System.out.println(id);
		 biddingService.updateBidStatus(id,"SHIPPING");
		 biddingService.setShippingNo(id,data);

		 return "redirect:/member/mypage/myauction";
	}

	@GetMapping(value = "member/mypage/answer/{auctionQueryNo}")
	public String answerList(Model model, Authentication authentication,
							 @PathVariable("auctionQueryNo") Long auctionQueryNo) {
		PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
		model.addAttribute("auctionQueryResponseDto", new AuctionQueryResponseDto());

		Member member = principalDetails.getMember();

		model.addAttribute("auctionQueryNo", auctionQueryNo);

		return "member/answer";
	}


	@PostMapping(value = "/member/mypage/answer/{auctionQueryNo}")
	public String addQueryResponse(@Valid AuctionQueryResponseDto auctionQueryResponseDto,
								   Authentication authentication, BindingResult bindingResult, Model model,
								   @PathVariable("auctionQueryNo") Long auctionQueryNo) {
		PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
		if (bindingResult.hasErrors()) {
			model.addAttribute("errorMessage", "답변하기중 오류가 발생했습니다.");
			model.addAttribute("auctionQueryResponseDto", auctionQueryResponseDto);
			return "member/mypage/answer";
		}

		try {
			auctionService.createdQueryResponse(auctionQueryResponseDto, principalDetails.getUsername(),
					auctionQueryNo);
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("auctionQueryResponseDto", new AuctionQueryResponseDto());


			return "member/mypage/answer";
		}

		return "redirect:/";
	}
	

	@GetMapping(value = "/member/querydetails/{auctionQueryNo}")
	public String memberQueryDetail(@PathVariable("auctionQueryNo") Long auctionQueryNo , Model model
			,  Authentication authentication) {
		PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
		Member member = principalDetails.getMember();
		
		try {
			AuctionQuery auctionQuery = auctionService.getAuctionDtl(auctionQueryNo);
			System.out.println("here");
			model.addAttribute("auctionQuery",auctionQuery);
		} catch (Exception e) {
		model.addAttribute("errorMessage" , "상품정보를 가져오지 못했습니다.");
		
		return "member/receivedquery";
				
		}
		
		return "/member/querydetails";
		
		
	}
}
