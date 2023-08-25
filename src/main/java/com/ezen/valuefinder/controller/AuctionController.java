
package com.ezen.valuefinder.controller;

import com.ezen.valuefinder.config.PrincipalDetails;
import com.ezen.valuefinder.constant.AuctionStatus;
import com.ezen.valuefinder.constant.AuctionType;

import com.ezen.valuefinder.dto.AuctionQueryDto;
import com.ezen.valuefinder.dto.NormalAuctionFormDto;

import com.ezen.valuefinder.entity.AuctionQuery;

import com.ezen.valuefinder.dto.ReverseAuctionFormDto;
import com.ezen.valuefinder.entity.Auction;
import com.ezen.valuefinder.entity.Bank;

import com.ezen.valuefinder.dto.*;
import com.ezen.valuefinder.entity.Auction;
import com.ezen.valuefinder.entity.Bidding;

import com.ezen.valuefinder.entity.Category;
import com.ezen.valuefinder.entity.Member;
import com.ezen.valuefinder.service.AuctionService;
import com.ezen.valuefinder.service.BiddingService;
import com.ezen.valuefinder.service.MemberService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import com.ezen.valuefinder.dto.NormalAuctionFormDto;

import java.security.Principal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class AuctionController {

	private final AuctionService auctionService;
	private final BiddingService biddingService;

	@GetMapping(value = "/auction/add")
	public String addItem(Model model) {
		List<Category> categoryList = auctionService.getCategoryList();
		model.addAttribute("categoryList", categoryList);
		model.addAttribute("normalAuctionFromDto", new NormalAuctionFormDto());
		return "/auction/form/normalitemform";
	}

	@PostMapping(value = "/auction/add")
	public String addItem(@Valid NormalAuctionFormDto normalAuctionFormDto, BindingResult bindingResult, Model model,
			@RequestParam("image") List<MultipartFile> itemImgFiles, Authentication authentication) {
		PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

		if (bindingResult.hasErrors()) {
			List<Category> categoryList = auctionService.getCategoryList();
			model.addAttribute("categoryList", categoryList);
			model.addAttribute("normalAuctionFromDto", new NormalAuctionFormDto());
			return "/auction/form/normalitemform";
		}

		if (bindingResult.hasErrors()) {
			List<Category> categoryList = auctionService.getCategoryList();
			model.addAttribute("categoryList", categoryList);
			model.addAttribute("normalAuctionFromDto", new NormalAuctionFormDto());
			return "/auction/form/normalitemform";
		}

		if (itemImgFiles.get(0).isEmpty()) {
			model.addAttribute("errorMessage", "게시글 첫번째 이미지는 필수입니다.");
			List<Category> categoryList = auctionService.getCategoryList();
			model.addAttribute("categoryList", categoryList);
			model.addAttribute("normalAuctionFromDto", new NormalAuctionFormDto());
			return "/auction/form/normalitemform";
		}
		try {
			auctionService.createAuction(normalAuctionFormDto, itemImgFiles, principalDetails.getUsername());
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

		auctionService.createReverseAuction(reverseAuctionFormDto, email);

		return "redirect:/";
	}

	@GetMapping(value = "/auction/public/detail")
	public String publicBidDetail(Model model) {
		Auction auction = auctionService.getAuction(1L);
		model.addAttribute("remainTime", auctionService.getRemainTime(auction.getAuctionEndTime()));
		model.addAttribute("auction", auction);
		model.addAttribute("nowTime", LocalDateTime.now());
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

	// 문의하기 insery
	@PostMapping(value = "/auction/query/add/{auctionNo}")
	public String addQuery(@Valid AuctionQueryDto auctionQueryDto, Authentication authentication,
			BindingResult bindingResult, Model model, @PathVariable("auctionNo") Long auctionNo) {
		PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

		if (bindingResult.hasErrors()) {

			model.addAttribute("auctionQueryDto", auctionQueryDto);
			return "auction/query/add/{auctionNo}";
		}

		try {
			auctionService.createdQuery(auctionQueryDto, principalDetails.getUsername(), auctionNo);
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("auctionQueryDto", new AuctionQueryDto());

			return "auction/query/add/{auctionNo}";
		}

		return "redirect:/";
	}

	@GetMapping(value = "/auction/query/add/{auctionNo}")
	public String auctionQuery(Model model, Authentication authentication, @PathVariable("auctionNo") Long auctionNo) {
		model.addAttribute("auctionQueryDto", new AuctionQueryDto());
		PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
		Member member = principalDetails.getMember();
		model.addAttribute("member", member);
		model.addAttribute("auctionNo", auctionNo);
		
		return "/auction/query/queryform";
	}

	@PostMapping(value = "/auction/query/{auctionQueryNo}")
	public String queryUpdate(@Valid AuctionQueryDto auctionQueryDto, Model model, Authentication authentication,
			BindingResult bindingResult , AuctionQuery auctionQuery , @PathVariable("auctionQueryNo") Long auctionQueryNo) {
		PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
		model.addAttribute("auctionQuery " , auctionQuery);
		
		
		if (bindingResult.hasErrors()) {
		 model.addAttribute("auctinoQueryNo" , auctionQueryNo);
			return "auction/query/queryform";
		}

		try {
			auctionService.updateQuery(auctionQueryDto, principalDetails.getUsername() , auctionQueryNo);
		} catch (Exception e) {
			model.addAttribute("errorMessage", "문의 글 수정중 에러가 발생했습니다");
			return "auction/query/queryform";

		}

		return "redirect:/";
	}

	// 문의 하기 수정페이지
	@GetMapping(value = "/auction/query/{auctionQueryNo}")
	public String queryDetail(@PathVariable("auctionQueryNo") Long auctionQueryNo, Model model,
			Authentication authentication) {
		PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
		Member member = principalDetails.getMember();
		
		try {
			AuctionQuery auctionQuery  = auctionService.getAuctionDtl(auctionQueryNo);
			model.addAttribute("auctionQuery",auctionQuery);
		} catch (Exception e) {
			model.addAttribute("errorMessage" , "상품정보를 가져올때 에러가 발생했습니다.");
			
			model.addAttribute("auctionQueryDto" , new AuctionQueryDto());
			return "auction/query/queryForm";
		}
		
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

	@GetMapping(value = "auction/public/detail/{auctionNo}")
	public String publicBidDetail(Model model, @PathVariable("auctionNo") Long auctionNo) {
		Auction auction = auctionService.getAuctionDetail(auctionNo);
		auctionService.auctionCount(auctionNo);
		model.addAttribute("remainTime", auctionService.getRemainTime(auction.getAuctionEndTime()));
		model.addAttribute("auction", auction);
		model.addAttribute("nowTime", LocalDateTime.now());

		return "/auction/details/publicDetail";
	}

	@GetMapping(value = "/auction/realtime/detail/{auctionNo}")
	public String realtimeBidDetail(Model model, @PathVariable("auctionNo") Long auctionNo) {
		Auction auction = auctionService.getAuctionDetail(auctionNo);
		auctionService.auctionCount(auctionNo);
		model.addAttribute("remainTime", auctionService.getRemainTime(auction.getAuctionEndTime()));
		model.addAttribute("auction", auction);
		model.addAttribute("nowTime", LocalDateTime.now());
		return "/auction/details/realtimeDetail";
	}

	@GetMapping(value = "/auction/sealed/detail/{auctionNo}")
	public String sealedBidDetail(Model model, @PathVariable("auctionNo") Long auctionNo) {
		Auction auction = auctionService.getAuctionDetail(auctionNo);
		auctionService.auctionCount(auctionNo);
		model.addAttribute("remainTime", auctionService.getRemainTime(auction.getAuctionEndTime()));
		model.addAttribute("auction", auction);
		model.addAttribute("nowTime", LocalDateTime.now());
		return "/auction/details/sealedDetail";
	}

	@PostMapping(value = "/auction/bidding")
	public @ResponseBody ResponseEntity bidding(@RequestBody Map<String, Object> requestBody,
			Authentication authentication) {
		PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

		try {
			Long auctionNo = Long.parseLong(requestBody.get("auctionNo").toString());
			Long price = Long.parseLong(requestBody.get("price").toString());

			Auction auction = auctionService.getAuction(auctionNo);

			switch (auction.getAuctionStatus()) {
			case PENDING -> {
				return new ResponseEntity("경매 시작 전입니다.", HttpStatus.LOCKED);
			}
			case PROGRESS -> {
				if (price <= auction.getAuctionNowPrice()) {
					return new ResponseEntity("현재 금액보다 큰 금액을 입력해주세요.", HttpStatus.BAD_REQUEST);
				}
				biddingService.chkStatus(auction);
				return new ResponseEntity("등록 완료", HttpStatus.OK);
			}
			case END -> {
				return new ResponseEntity("종료된 경매입니다.", HttpStatus.LOCKED);
			}
			case LAST -> {
				if (price <= auction.getAuctionNowPrice()) {
					return new ResponseEntity("현재 금액보다 큰 금액을 입력해주세요.", HttpStatus.BAD_REQUEST);
				}
				biddingService.chkStatus(auction);
				return new ResponseEntity("등록 완료", HttpStatus.OK);
			}
			}

			biddingService.joinBidding(principalDetails.getUsername(), auctionNo, price);
		} catch (Exception e) {
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity(HttpStatus.OK);
	}

	@GetMapping(value = "/auction/query")
	public String queryDetail() {
		return "/auction/query/query";
	}

	// 실시간 경매 페이지
	@GetMapping(value = { "/auction/realtime", "/auction/realtime/{page}" })
	public String auctionRealtime(Optional<Integer> page, Model model) {
		Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 30);
		Page<Auction> auctionList = auctionService.getAuctionList(pageable, AuctionType.REALTIME);
		auctionList.forEach(auction -> {
			Duration remainingDuration = Duration.between(LocalDateTime.now(), auction.getAuctionEndTime());
			long hours = remainingDuration.toHours();
			long minutes = remainingDuration.minusHours(hours).toMinutes();
			long seconds = remainingDuration.minusHours(hours).minusMinutes(minutes).getSeconds();
			if (remainingDuration.isNegative() || remainingDuration.isZero()) {
				auction.setRemainingTime("종료된 경매입니다.");
			} else {
				auction.setRemainingTime(hours + "시간" + minutes + "분" + seconds + "초");
			}
		});
		model.addAttribute("nowTime", LocalDateTime.now());
		model.addAttribute("auctionList", auctionList);
		model.addAttribute("maxPage", 5);
		return "auction/realtime";
	}

	// 역경매
	@GetMapping(value = "/auction/reversebid")
	public String auctionReversebid() {

		return "auction/reversebid";
	}

	@GetMapping(value = { "/auction/sealedbid", "/auction/sealedbid/{page}" })
	public String auctionSealedbid(Optional<Integer> page, Model model) {
		Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 30);
		Page<Auction> auctionList = auctionService.getAuctionList(pageable, AuctionType.SEALED);
		auctionList.forEach(auction -> {
			Duration remainingDuration = Duration.between(LocalDateTime.now(), auction.getAuctionEndTime());
			long hours = remainingDuration.toHours();
			long minutes = remainingDuration.minusHours(hours).toMinutes();
			long seconds = remainingDuration.minusHours(hours).minusMinutes(minutes).getSeconds();
			if (remainingDuration.isNegative() || remainingDuration.isZero()) {
				auction.setRemainingTime("종료된 경매입니다.");
			} else {
				auction.setRemainingTime(hours + "시간" + minutes + "분" + seconds + "초");
			}
		});
		model.addAttribute("nowTime", LocalDateTime.now());
		model.addAttribute("auctionList", auctionList);
		model.addAttribute("maxPage", 5);

		return "auction/sealedbid";
	}

	// 비공개 경매 페이지
	@GetMapping(value = "/auction/reversebid/details")
	public String redetails() {

		return "auction/reversebid/details";
	}
	
	@DeleteMapping("/auction/query/{auctionQueryNo}/delete")
	public @ResponseBody ResponseEntity<Long> deleteQuery(@PathVariable("auctionQueryNo") Long auctionQueryNo
			, Authentication authentication) {
		PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
		
		
		auctionService.deleteQuery(auctionQueryNo);
		
		return new ResponseEntity<Long>(auctionQueryNo , HttpStatus.OK);
	}

}
