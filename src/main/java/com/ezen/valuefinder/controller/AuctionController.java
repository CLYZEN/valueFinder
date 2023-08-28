package com.ezen.valuefinder.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.example.apply.dto.QnaDto;
import com.ezen.valuefinder.config.PrincipalDetails;
import com.ezen.valuefinder.constant.AuctionType;
import com.ezen.valuefinder.dto.AuctionQueryDto;
import com.ezen.valuefinder.dto.AuctionReportDto;
import com.ezen.valuefinder.dto.NormalAuctionFormDto;
import com.ezen.valuefinder.dto.ReverseAuctionFormDto;
import com.ezen.valuefinder.dto.ReversebidEnterDto;
import com.ezen.valuefinder.entity.Auction;
import com.ezen.valuefinder.entity.Category;
import com.ezen.valuefinder.entity.Member;
import com.ezen.valuefinder.entity.ReverseBidding;
import com.ezen.valuefinder.service.AuctionReportService;
import com.ezen.valuefinder.service.AuctionService;
import com.ezen.valuefinder.service.BiddingService;
import com.ezen.valuefinder.service.ReversebidService;
import com.ezen.valuefinder.service.WishService;
import com.shopmax.dto.ItemFormDto;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@Controller
@RequiredArgsConstructor
public class AuctionController {


    private final AuctionService auctionService;
    private final BiddingService biddingService;
    private final ReversebidService reversebidService;
    private final WishService wishService;


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


    @GetMapping(value = "auction/detail/{auctionNo}")
    public String auctionDetail(Model model, @PathVariable("auctionNo") Long auctionNo,Authentication authentication, Optional<Integer> page) {
        Auction auction = auctionService.getAuctionDetail(auctionNo);
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        boolean checkWish = wishService.checkWish(auctionNo, principalDetails.getMember().getMemberId());
        model.addAttribute("auction", auction);
        model.addAttribute("checkWish", checkWish);
        auctionService.addAuctionView(auctionNo);
        
        if(auction.getAuctionType() == AuctionType.PUBLIC) {
            model.addAttribute("remainTime", auctionService.getRemainTime(auction.getAuctionEndTime()));
            model.addAttribute("auction", auction);
            model.addAttribute("nowTime", LocalDateTime.now());
            model.addAttribute("itemCount", auctionService.itemCount(auction.getItem().getMember().getMemberId()));
            Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 6);
            model.addAttribute("auctionList",
                    auctionService.getMemberAuctionList(auction.getItem().getMember().getMemberId(), pageable));
            auctionService.updateAuction(auctionNo);
            return "/auction/details/publicDetail";
        } else if (auction.getAuctionType() == AuctionType.REALTIME) {
            model.addAttribute("remainTime", auctionService.getRemainTime(auction.getAuctionEndTime()));
            model.addAttribute("auction", auction);
            model.addAttribute("nowTime", LocalDateTime.now());
            model.addAttribute("itemCount", auctionService.itemCount(auction.getItem().getMember().getMemberId()));
            Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 6);
            model.addAttribute("auctionList",
                    auctionService.getMemberAuctionList(auction.getItem().getMember().getMemberId(), pageable));
            auctionService.updateAuction(auctionNo);
            return "/auction/details/realtimeDetail";
        } else {
            model.addAttribute("remainTime", auctionService.getRemainTime(auction.getAuctionEndTime()));
            model.addAttribute("auction", auction);
            model.addAttribute("nowTime", LocalDateTime.now());
            model.addAttribute("itemCount", auctionService.itemCount(auction.getItem().getMember().getMemberId()));
            Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 6);
            model.addAttribute("auctionList",
                    auctionService.getMemberAuctionList(auction.getItem().getMember().getMemberId(), pageable));
            auctionService.updateAuction(auctionNo);
            return "/auction/details/sealedDetail";
        }
    }

    @PostMapping(value = "/auction/query/add")
    public String addQuery(@Valid AuctionQueryDto auctionQueryDto, Principal principal, BindingResult bindingResult,
                           Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("auctionQueryDto", auctionQueryDto);
            return "/auction/query/queryform";
        }

        try {
            auctionService.createdQuery(auctionQueryDto, principal.getName());
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("auctionQueryDto", new AuctionQueryDto());
            return "/auction/query/queryform";
        }

        return "redirect:/";
    }

    @GetMapping(value = "/auction/query/add")
    public String auctionQuery(Model model, Authentication authentication) {
        model.addAttribute("auctionQueryDto", new AuctionQueryDto());
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        Member member = principalDetails.getMember();
        model.addAttribute("member", member);

        return "/auction/query/queryform";
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
                case PROGRESS, LAST -> {
                    if (price <= auction.getAuctionNowPrice()) {
                        return new ResponseEntity("현재 금액보다 큰 금액을 입력해주세요.", HttpStatus.BAD_REQUEST);
                    }
                    biddingService.chkStatus(auction);
                    return new ResponseEntity("등록 완료", HttpStatus.OK);
                }
                case END -> {
                    return new ResponseEntity("종료된 경매입니다.", HttpStatus.LOCKED);
                }
            }
            biddingService.joinBidding(principalDetails.getUsername(), auctionNo, price);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    //경매 신고 페이지 띄우기
    @GetMapping(value = "/auction/report")
    public String reportAuction() {
        return "/auction/report";
    }
    
    //경매 신고글 등록하기
    @PostMapping(value = "/auction/report")
    public String addReportAuction(@Valid AuctionReportDto auctionReportDto, Principal principal) {
    	
    	try {
    		AuctionReportService.saveReport(auctionReportDto, auctionReportNo);
		} catch (Exception e) {
			 e.printStackTrace();
		}
    	
    	return "redirect:/auction/report";
    }
    
    

    @GetMapping(value = "/auction/query")
    public String queryDetail() {
        return "/auction/query/query";
    }

    @GetMapping(value = "/auction/reversebid/enter/add")
    public String enterQuery() {
        return "/auction/enter/enterForm";
    }

    @GetMapping(value = "/auction/reversebid/enter")
    public String enterDetail() {
        return "/auction/enter/enter";
    }


    @GetMapping(value = "/auction/reversebid/enter/add/{bidno}")
    public String enterReversebid(Authentication authentication, Model model, @PathVariable Long bidno) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        Member member = principalDetails.getMember();
        ReverseBidding reverseBidding = reversebidService.getReversebidById(bidno);


        model.addAttribute("reversebidEnterDto", new ReversebidEnterDto());
        model.addAttribute("bid", reverseBidding);
        model.addAttribute("member", member);

        return "/auction/enter/enterForm";
    }

    @PostMapping(value = "/auction/reversebid/enter/add/{bidno}")
    public String enterReversebid(Authentication authentication, Model model, @Valid ReversebidEnterDto reversebidEnterDto
            , @RequestParam("image") List<MultipartFile> itemImgFiles, @PathVariable Long bidno) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        Member member = principalDetails.getMember();
        ReverseBidding reverseBidding = reversebidService.getReversebidById(bidno);
        if (itemImgFiles.get(0).isEmpty()) {
            model.addAttribute("errorMessage", "첫번째 이미지는 필수입니다.");
            model.addAttribute("reversebidEnterDto", new ReversebidEnterDto());
            model.addAttribute("bid", reverseBidding);
            model.addAttribute("member", member);
            return "/auction/enter/enterForm";
        }
        try {
            reversebidService.saveReversebidEnter(reversebidEnterDto, member, bidno, itemImgFiles);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "첫번째 이미지는 필수입니다.");
            model.addAttribute("reversebidEnterDto", new ReversebidEnterDto());
            model.addAttribute("bid", reverseBidding);
            model.addAttribute("member", member);
            return "/auction/enter/enterForm";
        }

        return "redirect:/auction/reversebid/enter/" + bidno;
    }


    //역경매
    @GetMapping(value = "/auction/reversebid")
    public String auctionReversebid() {

        return "auction/reversebid";
    }
    
    // 실시간 경매 페이지
    @GetMapping(value = {"/auction/realtime", "/auction/realtime/{page}"})
    public String auctionRealtime(Optional<Integer> page, Model model,@RequestParam Long category) {
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 30);
        Page<Auction> auctionList = auctionService.getAuctionList(pageable, AuctionType.REALTIME,category);

        for (Auction auction : auctionList) {
            auctionService.updateAuction(auction.getAuctionNo());
        }

        model.addAttribute("nowTime", LocalDateTime.now());
        model.addAttribute("auctionList", auctionList);
        model.addAttribute("maxPage", 5);
        return "auction/realtime";
    }
    
    @GetMapping(value = {"/auction/sealedbid", "/auction/sealedbid/{page}"})
    public String auctionSealedbid(Optional<Integer> page, Model model,@RequestParam Long category) {
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 30);
        Page<Auction> auctionList = auctionService.getAuctionList(pageable, AuctionType.SEALED,category);

        for (Auction auction : auctionList) {
            auctionService.updateAuction(auction.getAuctionNo());
        }


        model.addAttribute("nowTime", LocalDateTime.now());
        model.addAttribute("auctionList", auctionList);
        model.addAttribute("maxPage", 5);

        return "auction/sealedbid";
    }

    @GetMapping(value = {"/auction/publicbid", "/auction/publicbid/{page}"})
    public String auctionPublicbid(Optional<Integer> page, Model model,@RequestParam("category") Long category) {
        Page<Auction> auctionList;
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 30);

        auctionList = auctionService.getAuctionList(pageable, AuctionType.PUBLIC,category);


        for (Auction auction : auctionList) {
            auctionService.updateAuction(auction.getAuctionNo());
        }

        model.addAttribute("nowTime", LocalDateTime.now());
        model.addAttribute("auctionList", auctionList);
        model.addAttribute("maxPage", 5);

        return "auction/publicbid";
    }

    //비공개 경매 페이지
    @GetMapping(value = "/auction/reversebid/details")
    public String redetails() {

        return "auction/reversebid/details";
    }

}

