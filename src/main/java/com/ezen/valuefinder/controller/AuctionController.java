package com.ezen.valuefinder.controller;

import com.ezen.valuefinder.config.PrincipalDetails;
import com.ezen.valuefinder.constant.AuctionType;
import com.ezen.valuefinder.dto.ItemSearchDto;
import com.ezen.valuefinder.dto.NormalAuctionFormDto;
import com.ezen.valuefinder.dto.ReverseAuctionFormDto;
import com.ezen.valuefinder.entity.Auction;
import com.ezen.valuefinder.entity.Bank;
import com.ezen.valuefinder.entity.Category;
import com.ezen.valuefinder.entity.Item;
import com.ezen.valuefinder.service.AuctionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class AuctionController {

	private final AuctionService auctionService;

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
	public String addReverseItem(Model model) {
		List<Category> categoryList = auctionService.getCategoryList();
		model.addAttribute("reverseAuctionFromDto", new ReverseAuctionFormDto());
		model.addAttribute("categoryList", categoryList);
		return "/auction/form/reverseitemform";
	}

	@PostMapping(value = "/auction/reverse/add")
	public String addReverseItem(@Valid ReverseAuctionFormDto reverseAuctionFormDto, Authentication authentication) {
		PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
		String email = principalDetails.getUsername();

		auctionService.createReverseAuction(reverseAuctionFormDto,email);

		return "redirect:/";
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

	@GetMapping(value = "/auction/query/add")
	public String auctionQuery() {
		return "/auction/query/queryform";
	}

	@GetMapping(value = "/auction/query")
	public String queryDetail() {
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
	@GetMapping(value={"/auction/realtime", "items/{page}"})
	public String auctionRealtime(Model model, ItemSearchDto itemSearchDto,@PathVariable("page") Optional<Integer> page) {

		Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0 , 3);
		Page<Item> items = auctionService.getAuctionPage(itemSearchDto, pageable);
		List<Auction> auctions = auctionService.getAuctionList();
		
		model.addAttribute("items", items);
		model.addAttribute("auctions",auctions);
		model.addAttribute("itemSearchDto", itemSearchDto);
		model.addAttribute("maxPage", 5);
		
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
