package com.ezen.valuefinder.controller;

import com.ezen.valuefinder.config.PrincipalDetails;
import com.ezen.valuefinder.constant.AuctionType;
import com.ezen.valuefinder.constant.BidStatus;
import com.ezen.valuefinder.dto.*;
import com.ezen.valuefinder.entity.*;
import com.ezen.valuefinder.repository.BiddingRepository;
import com.ezen.valuefinder.repository.CouponRepository;
import com.ezen.valuefinder.service.AuctionQueryService;
import com.ezen.valuefinder.service.AuctionService;
import com.ezen.valuefinder.service.BiddingService;
import com.ezen.valuefinder.service.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.method.P;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


import java.io.Console;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class MemberController {

	private final MemberService memberService;
	private final PasswordEncoder passwordEncoder;
	private final AuctionService auctionService;
	private final BiddingService biddingService;
	private final AuctionQueryService auctionQueryService;
	private final CouponRepository couponRepository;

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

		 BiddingCountsDTO publicBidCount = biddingService.getMypageCount(AuctionType.PUBLIC,principalDetails.getMember());
		 BiddingCountsDTO sealedBidCount =  biddingService.getMypageCount(AuctionType.SEALED,principalDetails.getMember());
		 BiddingCountsDTO realtimeBidCount = biddingService.getMypageCount(AuctionType.REALTIME,principalDetails.getMember());

		 model.addAttribute("realtimeBidCount",realtimeBidCount);
		 model.addAttribute("sealedBidCount",sealedBidCount);
		 model.addAttribute("publicBidCount",publicBidCount);
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

		 for(SuccessBidding successBidding : successBiddingList) {
			 biddingService.chkStatus(successBidding.getAuction());
		 }

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
		 Page<MemberMyAuctionDto> auctionList = auctionService.getMemberAuctionList(member.getMemberId(),pageable);
		 List<Auction> auctions = new ArrayList<>();
		 for(MemberMyAuctionDto dto : auctionList.getContent()) {
			 auctions.add(auctionService.getAuction(dto.getAuctionNo()));
		 }
		 for(Auction auction : auctions) {
			 biddingService.chkStatus(auction);
		 }

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

	 @GetMapping(value = {"member/mypage/todayview","member/mypage/todayview/{page}"})
	 public String todayViewAuction(HttpServletRequest request,Model model,Authentication authentication, @PathVariable("page") Optional<Integer> page) {
		 PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
		 Member member = principalDetails.getMember();

		 String cookieName = "auctionNoCookie";
		 // 쿠키에서 기존 auctionNo 가져오기
		 String existingAuctionNos = null;
		 Cookie[] cookies = request.getCookies();
		 if (cookies != null) {
			 for (Cookie cookie : cookies) {
				 if (cookie.getName().equals(cookieName)) {
					 existingAuctionNos = cookie.getValue();
					 break;
				 }
			 }
		 }

		 // 쿠키에서 가져온 문자열을 '/'로 나누어 배열로 변환
		 String[] auctionNosArray = existingAuctionNos != null ? existingAuctionNos.split("/") : new String[0];
		 List<Long> auctionNoList = Arrays.stream(auctionNosArray)
				 .map(Long::parseLong)
				 .collect(Collectors.toList());
		 Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 4);

		Page<Auction> auctionList = auctionService.getTodayViewAuctionList(auctionNoList,pageable);

		for(Auction auction : auctionList) {
			biddingService.chkStatus(auction);
		}

		model.addAttribute("maxPage",5);
		model.addAttribute("member",member);
		model.addAttribute("auctionList",auctionList);
		 return "member/todayviewauction";
	 }

	 @GetMapping(value ="member/mypage/modify/password")
	 public String changepwd(Model model, Authentication authentication) {
		 PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
		 Member member = principalDetails.getMember();
		 model.addAttribute("modifyPasswordDto",new ModifyPasswordDto());
		 model.addAttribute("member",member);
		 return "member/changepwd";
	 }

	@PostMapping(value ="member/mypage/modify/password")
	public String changepwd(Model model, Authentication authentication,@Valid ModifyPasswordDto modifyPasswordDto,BindingResult bindingResult) {
		PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
		Member member = principalDetails.getMember();
		boolean passwordResult = passwordEncoder.matches(modifyPasswordDto.getNowPassword(),member.getPassword());

		if(bindingResult.hasErrors()) {
			model.addAttribute("member",member);
			return "member/changePwd";
		}

		if (!modifyPasswordDto.getUpdatePassword().equals(modifyPasswordDto.getConfirmPassword())) {
			model.addAttribute("errorMessage","비밀번호가 같지 않습니다.");
			model.addAttribute("modifyPasswordDto",new ModifyPasswordDto());
			model.addAttribute("member",member);
			return "member/changePwd";
		} else if (!passwordResult) {
			model.addAttribute("errorMessage","현재 비밀번호가 다릅니다.");
			model.addAttribute("modifyPasswordDto",new ModifyPasswordDto());
			model.addAttribute("member",member);
			return "member/changePwd";
		}
		memberService.updatePwd(modifyPasswordDto.getUpdatePassword(),principalDetails.getUsername(),passwordEncoder);
		return "redirect:/";
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
	 public String coupon(Model model,Authentication authentication,@PathVariable("page") Optional<Integer> page) {
		 PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
		 Member member = principalDetails.getMember();
		 Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 4);

		 Page<Coupon> couponPage = couponRepository.findByMember(member,pageable);
		 model.addAttribute("maxPage",5);
		 model.addAttribute("couponPage",couponPage);
		 model.addAttribute("member",member);
		 return "member/coupon";
	 }
	 
	 @GetMapping(value ={"member/mypage/sentquery","member/mypage/sentquery/{page}"})
	 public String sentquery(Model model,Authentication authentication,@PathVariable("page") Optional<Integer> page) {
		 PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
		 Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 5);
		 Page<AuctionQuery> auctionQueryList = auctionQueryService.getAuctionQueryPage(pageable,principalDetails.getMember());

		 for (AuctionQuery auctionQuery : auctionQueryList) {
			 biddingService.chkStatus(auctionQuery.getAuction());
		 }
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
		 for (AuctionQuery auctionQuery : auctionQueryList) {
			 biddingService.chkStatus(auctionQuery.getAuction());
		 }
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

		 for(Wish wish : wishList) {
			 biddingService.chkStatus(wish.getAuction());
		 }

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
		memberService.deleteMemberOut(member);
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
	


}
