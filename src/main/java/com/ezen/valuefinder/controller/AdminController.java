package com.ezen.valuefinder.controller;

import com.ezen.valuefinder.config.PrincipalDetails;
import com.ezen.valuefinder.dto.CreateCouponDto;
import com.ezen.valuefinder.dto.MemberCautionDto;
import com.ezen.valuefinder.entity.Auction;
import com.ezen.valuefinder.entity.AuctionQuery;
import com.ezen.valuefinder.entity.AuctionReport;
import com.ezen.valuefinder.entity.CouponList;
import com.ezen.valuefinder.entity.Member;
import com.ezen.valuefinder.entity.MemberReport;
import com.ezen.valuefinder.repository.AuctionReportRepository;
import com.ezen.valuefinder.repository.MemberRepository;
import com.ezen.valuefinder.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.modelmapper.internal.util.Members;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class AdminController {
	private final MemberService memberService;
	private final CouponService couponService;
	private final MemberRepository memberRepository;
	private final AdminService adminService;
	private final AuctionService auctionService;
	@GetMapping(value = "/admin")
	public String adminMain() {

		return "admin/main";
	}

	@GetMapping(value = "/admin/itemList")
	public String adminitemlist(Model model) {

		List<Auction> auctions = auctionService.getItemAuctionList();
		model.addAttribute("auctions", auctions);

		return "admin/itemList";
	}

	/*
	 * @GetMapping(value= "/custiomerList") public String list(Model model , Long
	 * memberId) { Member members = adminService.getMember(memberId);
	 *
	 * model.addAttribute("members",members); return "admin/custiomerList"; }
	 */

	@PostMapping(value = "/admin/customerList")
	public @ResponseBody ResponseEntity updateCaution(@RequestBody @Valid MemberCautionDto memberCautionDto) {
		try {
			memberService.memberCaution(memberCautionDto.getMemberId());
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<Long>(memberCautionDto.getMemberId(), HttpStatus.OK); // 성공시

	}

	@GetMapping(value = "/admin/customerList")
	public String admincustomerList(Model model) {

		List<Member> members = memberService.getMemberList();

		model.addAttribute("members", members);

		return "admin/customerList";
	}

	@GetMapping(value = "/admin/itemreportList")
	public String adminitemreportList(Model model) {
		List<AuctionReport> auctionReports = auctionService.getAuctionReportList();
		model.addAttribute("auctionReports",auctionReports);

		return "admin/itemreportList";
	}

	@DeleteMapping("/auction/itemreportList/{auctionNo}/delete")
	public @ResponseBody ResponseEntity<Long> deleteQuery(@PathVariable("auctionNo") Long auctionNo
	) {



		auctionService.deleteQuery(auctionNo);

		return new ResponseEntity<Long>(auctionNo , HttpStatus.OK);
	}



	@GetMapping(value = "/admin/customerReportList")
	public String admincustomerReportList(Model model) {
		List<MemberReport> memberReports = memberService.getMemberReportList();

		model.addAttribute("memberReports" ,memberReports);


		return "admin/customerReportList";
	}

	@GetMapping(value = "/admin/customerStatus")
	public String admincustomerStatus() {
		return "admin/customerStatus";
	}

	@GetMapping(value = "/admin/itemStatus")
	public String adminitemStatus() {
		return "admin/itemStatus";
	}

	@GetMapping(value = "/admin/coupon")
	public String couponManage(Model model) {
		List<CouponList> couponList = couponService.getCouponList();
		model.addAttribute("couponList", couponList);
		return "admin/coupon";
	}

	@GetMapping(value = "/admin/coupon/create")
	public String createCoupon(Model model) {
		model.addAttribute("createCouponDto", new CreateCouponDto());
		return "admin/createcoupon";
	}

	@PostMapping(value = "/admin/coupon/create")
	public String createCoupon(@Valid CreateCouponDto createCouponDto, BindingResult bindingResult, Model model) {

		if (bindingResult.hasErrors()) {
			model.addAttribute("createCouponDto", new CreateCouponDto());
			return "admin/createcoupon";
		}
		couponService.createCoupon(createCouponDto);

		return "redirect:/admin";
	}

	@DeleteMapping("/admin/itemreportList/{auctionNo}/delete")
	public @ResponseBody ResponseEntity<Long> deleteItem(@PathVariable("auctionNo") Long auctionNo
			, Authentication authentication) {
		PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
		adminService.deleteAuction(auctionNo);

		return new ResponseEntity<Long>(auctionNo , HttpStatus.OK);

	}
}