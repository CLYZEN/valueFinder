package com.ezen.valuefinder.controller;

import com.ezen.valuefinder.config.PrincipalDetails;
import com.ezen.valuefinder.dto.MemberFindPwDto;
import com.ezen.valuefinder.dto.MemberFormDto;
import com.ezen.valuefinder.dto.MemberModifyDto;
import com.ezen.valuefinder.dto.AuctionQueryDto;
import com.ezen.valuefinder.dto.AuctionQueryResponseDto;
import com.ezen.valuefinder.entity.Auction;
import com.ezen.valuefinder.entity.AuctionQuery;
import com.ezen.valuefinder.entity.AuctionQueryResponse;
import com.ezen.valuefinder.entity.Bank;
import com.ezen.valuefinder.entity.Item;
import com.ezen.valuefinder.entity.Member;
import com.ezen.valuefinder.repository.ItemRepository;
import com.ezen.valuefinder.service.AuctionService;
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
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class MemberController {

	private final MemberService memberService;
	private final PasswordEncoder passwordEncoder;
	private final AuctionService auctionService;
	private final ItemRepository itemRepository;

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
		if (bindingResult.hasErrors()) {
			model.addAttribute("registChk", "Check");
			model.addAttribute("bankList", bankList);
			model.addAttribute("memberFromDto", new MemberFormDto());
			return "member/register";
		}

		try {
			Member member = memberService.createMember(memberFormDto, passwordEncoder);
		} catch (Exception e) {
			model.addAttribute("registChk", "Check");
			model.addAttribute("errorMessage", e.getMessage());
			model.addAttribute("bankList", bankList);
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
	public String findPw(@Valid MemberFindPwDto memberFindPwDto, Model model) {

		if (memberService.findPwChkMember(memberFindPwDto) == null) {
			model.addAttribute("findPwDto", new MemberFindPwDto());
			model.addAttribute("errorMessage", "일치하는 회원 정보가 없습니다.");
			return "member/findpw";
		}

		model.addAttribute("email", memberFindPwDto.getEmail());
		return "member/resetpw";
	}

	@PostMapping(value = "/member/updatepw")
	public String updatePw(@Valid String password, @Valid String email) {
		memberService.updatePwd(password, email, passwordEncoder);

		return "member/login";
	}

	@GetMapping(value = "member/regist/social")
	public String socialRegist() {
		return "member/social";
	}

	@GetMapping(value = "member/mypage")
	public String myPage(Model model, Authentication authentication) {
		PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
		Member member = principalDetails.getMember();
		model.addAttribute("member", member);
		return "member/mypage";
	}

	@GetMapping(value = "member/mypage/bidding")
	public String bidding(Authentication authentication, Model model) {
		PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
		Member member = principalDetails.getMember();
		model.addAttribute("member", member);
		return "member/bidding";
	}

	@GetMapping(value = "member/mypage/successfulbid")
	public String successfulbid(Authentication authentication, Model model) {
		PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
		Member member = principalDetails.getMember();
		model.addAttribute("member", member);
		return "member/successfulbid";
	}

	@GetMapping(value = "member/mypage/myauction")
	public String myauction(Model model, Authentication authentication) {
		PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
		Member member = principalDetails.getMember();
		model.addAttribute("member", member);
		return "member/myauction";
	}

	@GetMapping(value = "member/mypage/modify/checkpwd")
	public String checkpwd(Model model, Authentication authentication) {
		PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
		Member member = principalDetails.getMember();
		model.addAttribute("member", member);
		return "member/checkpwd";
	}

	@PostMapping(value = "member/mypage/modify/checkpwd")
	public String checkpwd(Model model, Authentication authentication, @Valid String password) {
		PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

		boolean result = memberService.checkPwd(password, principalDetails.getUsername(), passwordEncoder);
		if (result == true) {
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

	@GetMapping(value = "member/mypage/modify/password")
	public String changepwd(Model model, Authentication authentication) {
		PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
		Member member = principalDetails.getMember();
		model.addAttribute("member", member);
		return "member/changepwd";
	}

	@GetMapping(value = "member/mypage/outmember")
	public String outmember(Model model, Authentication authentication) {
		PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
		Member member = principalDetails.getMember();
		model.addAttribute("member", member);
		return "member/outmember";
	}

	@PostMapping(value = "member/mypage/outmember")
	public String outmember(@Valid String detail, Authentication authentication) {
		PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
		memberService.memberOut(principalDetails.getUsername(), detail);

		return "redirect:/member/logout";
	}

	@GetMapping(value = "member/mypage/coupon")
	public String coupon(Model model, Authentication authentication) {
		PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
		Member member = principalDetails.getMember();
		model.addAttribute("member", member);
		return "member/coupon";
	}

	@GetMapping(value = { "/member/mypage/sentquery", "/member/mypage/sentquery{page}" })
	public String sentquery(Model model, Authentication authentication, @PathVariable("page") Optional<Integer> page) {
		PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
		Member member = principalDetails.getMember();
		Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 4);

		Page<AuctionQuery> auctionQueryList = auctionService.auctionQueryList(pageable, member);

		model.addAttribute("member", member);
		model.addAttribute("auctionQueryList", auctionQueryList);
		model.addAttribute("maxPage", 5);

		return "member/sentquery";
	}

	@GetMapping(value = { "/member/mypage/receivedquery", "/member/mypage/receivedquery/{page}" })
	public String receivedquery(Model model, Authentication authentication,
			@PathVariable("page") Optional<Integer> page) {
		Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 4);
		PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
		Member member = principalDetails.getMember();
		Page<AuctionQueryResponse> auctionQueryResponseList = auctionService.auctionQueryResponseList(pageable, member);
	
		model.addAttribute("member", member);
		model.addAttribute("auctionQueryResponseList", auctionQueryResponseList);
		model.addAttribute("maxPage", 5);

		return "member/receivedquery";
	}

	@GetMapping(value = "member/mypage/like")
	public String like(Model model, Authentication authentication) {
		PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
		Member member = principalDetails.getMember();
		model.addAttribute("member", member);
		return "member/like";
	}

	@GetMapping(value = "member/mypage/modify")
	public String modify(Model model, Authentication authentication) {
		List<Bank> bankList = memberService.getBankList();
		PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
		Member member = principalDetails.getMember();

		model.addAttribute("memberModifyDto", new MemberModifyDto());
		model.addAttribute("member", member);
		model.addAttribute("bankList", bankList);
		return "member/modify";
	}

	// 에러메시지 구현 필요
	@PostMapping(value = "member/mypage/modify")
	public String modify(@Valid MemberModifyDto memberModifyDto, BindingResult bindingResult, Model model,
			Authentication authentication) {
		PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

		memberService.updateMember(memberModifyDto, principalDetails.getUsername());
		return "redirect:/";
	}

	// 계정복구
	@GetMapping(value = "repair")
	public String repairMember() {
		return "member/repairmember";
	}

	@PostMapping(value = "repair")
	public String repairMember(@Valid String email, @Valid String password, Model model) {
		Member member = memberService.findByEmail(email);
		boolean result = passwordEncoder.matches(password, member.getPassword());

		if (member == null || result == false) {
			model.addAttribute("errorMessage", "아이디 또는 비밀번호가 틀렸습니다.");
			return "member/repairmember";
		}
		memberService.repairMember(email);
		return "redirect:/member/login";
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
