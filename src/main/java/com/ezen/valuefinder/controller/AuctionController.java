package com.ezen.valuefinder.controller;

import com.ezen.valuefinder.config.PrincipalDetails;
import com.ezen.valuefinder.constant.AuctionType;
import com.ezen.valuefinder.dto.AuctionQueryDto;
import com.ezen.valuefinder.dto.NormalAuctionFormDto;
import com.ezen.valuefinder.entity.AuctionQuery;
import com.ezen.valuefinder.entity.Category;
import com.ezen.valuefinder.entity.Member;
import com.ezen.valuefinder.service.AuctionService;
import com.ezen.valuefinder.service.MemberService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class AuctionController {

	private final AuctionService auctionService;
	private final MemberService memberService;

	@GetMapping(value = "/auction/add")
	public String addItem(Model model) {
		List<Category> categoryList = auctionService.getCategoryList();
		model.addAttribute("categoryList", categoryList);
		model.addAttribute("normalAuctionFromDto", new NormalAuctionFormDto());
		return "/auction/form/normalitemform";
	}

	@PostMapping(value = "/auction/add")
	public String addItem(@Valid NormalAuctionFormDto normalAuctionFormDto, BindingResult bindingResult, Model model,
						  @RequestParam("image")List<MultipartFile> itemImgFiles, Authentication authentication) {
		PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

		if(bindingResult.hasErrors()) {
			List<Category> categoryList = auctionService.getCategoryList();
			model.addAttribute("categoryList", categoryList);
			model.addAttribute("normalAuctionFromDto", new NormalAuctionFormDto());
			return "/auction/form/normalitemform";
		}

		if(itemImgFiles.get(0).isEmpty()) {
			model.addAttribute("errorMessage", "게시글 첫번째 이미지는 필수입니다.");
			List<Category> categoryList = auctionService.getCategoryList();
			model.addAttribute("categoryList", categoryList);
			model.addAttribute("normalAuctionFromDto", new NormalAuctionFormDto());
			return "/auction/form/normalitemform";
		}
		try {
			auctionService.createAuction(normalAuctionFormDto,itemImgFiles,principalDetails.getUsername());
		} catch (Exception e) {
			e.printStackTrace();
			List<Category> categoryList = auctionService.getCategoryList();
			model.addAttribute("categoryList", categoryList);
			model.addAttribute("normalAuctionFromDto", new NormalAuctionFormDto());
			model.addAttribute("errorMessage", "등록 중 오류 발생");
			return "/auction/form/normalitemform";
		}
		return "redirect:/";
	}

	@GetMapping(value = "/auction/reverse/add")
	public String addReverseItem() {
		return "/auction/form/reverseitemform";
	}

	@GetMapping(value = "/auction/public/detail")
	public String publicBidDetail() {
		return "/auction/details/publicDetail";
	}
	
	@GetMapping(value = "/auction/realtime/detail")
	public String realtimeBidDetail() {
		return "/auction/details/realtimeDetail";
	}
	
	@GetMapping(value = "/auction/sealed/detail")
	public String sealedBidDetail() {
		return "/auction/details/sealedDetail";
	}
	//문의하기 insery
	@PostMapping(value = "/auction/query/add")
	public String addQuery(@Valid AuctionQueryDto auctionQueryDto ,Authentication authentication , BindingResult bindingResult , Model model  ) {
		PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
		if (bindingResult.hasErrors()) {
			model.addAttribute("auctionQueryDto",auctionQueryDto);
			return "/auction/query/add";
		}
		
		
		try {
			auctionService.createdQuery(auctionQueryDto , principalDetails.getUsername());
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("auctionQueryDto" , new AuctionQueryDto());
			
			return "/auction/query/add";
		}
		
		
		
	return "redirect:/";
	}
	
	
	@GetMapping(value = "/auction/query/add")
	public String auctionQuery(Model model,Authentication authentication) {
		model.addAttribute("auctionQueryDto",new AuctionQueryDto());
		PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
		Member member = principalDetails.getMember();
		model.addAttribute("member",member);
		
		
		
		return "/auction/query/queryform";
	}
	
	@PostMapping(value = "/auction/query/{auctionQueryNo}")
	public String queryUpdate(@Valid AuctionQueryDto auctionQueryDto, Model model , Authentication authentication , BindingResult bindingResult) {
		PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
		
		if(bindingResult.hasErrors()) {
			model.addAttribute("errorMessage" , "상품을 불러오지못했습니다");
			return "auction/query/{auctionQueryNo}";
		}
		
		
		try {
			auctionService.updateQuery(auctionQueryDto, principalDetails.getUsername() );
		} catch (Exception e) {
			model.addAttribute("errorMessage" , "상품 수정 중 에러가 발생했습니다");
			return "auction/query";
			
		}
		
		return "redirect:/";
	}
	
	
	//문의 하기 수정페이지
	@GetMapping(value = "/auction/query/{auctionQueryNo}")
	public String queryDetail(@PathVariable("quctionQueryNo") Long auctionQueryNo , Model model , Authentication authentication) {
		PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
		
		
	
		
		
		
		return "/auction/query/query";
	}

	@GetMapping(value = "/auction/report")
	public String reportAuction() {
		return "/auction/report";
	}

	@GetMapping(value = "/auction/reversebid/enter/add")
	public String enterQuery() {
		return "/auction/enter/enterForm";
	}
	
	@GetMapping(value = "/auction/reversebid/enter")
	public String enterDetail() {
		return "/auction/enter/enter";
	}


	//실시간 경매 페이지
	@GetMapping(value="/auction/realtime")
	public String auctionRealtime() {

		return "auction/realtime";
	}

	//역경매
	@GetMapping(value="/auction/reversebid")
	public String auctionReversebid() {

		return "auction/reversebid";
	}

	@GetMapping(value = "/auction/sealedbid")
	public String auctionSealedbid() {
		return "auction/sealedbid";
	}
	
	//비공개 경매 페이지
	@GetMapping(value="/auction/reversebid/details")
	public String redetails() {

		return "auction/reversebid/details";
	}

}
