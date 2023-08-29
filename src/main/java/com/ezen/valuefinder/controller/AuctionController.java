package com.ezen.valuefinder.controller;


import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;


import com.ezen.valuefinder.config.PrincipalDetails;
import com.ezen.valuefinder.constant.AuctionStatus;
import com.ezen.valuefinder.constant.AuctionType;
import com.ezen.valuefinder.dto.*;
import com.ezen.valuefinder.entity.*;
import com.ezen.valuefinder.repository.ReverseBiddingRepository;
import com.ezen.valuefinder.service.*;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.ezen.valuefinder.config.PrincipalDetails;
import com.ezen.valuefinder.constant.AuctionType;
import com.ezen.valuefinder.dto.AuctionQueryDto;
import com.ezen.valuefinder.dto.NormalAuctionFormDto;
import com.ezen.valuefinder.dto.ReverseAuctionFormDto;
import com.ezen.valuefinder.dto.ReversebidEnterDto;
import com.ezen.valuefinder.entity.Auction;
import com.ezen.valuefinder.entity.Category;
import com.ezen.valuefinder.entity.Member;
import com.ezen.valuefinder.entity.ReverseBidding;
import com.ezen.valuefinder.service.AuctionService;
import com.ezen.valuefinder.service.BiddingService;
import com.ezen.valuefinder.service.ReversebidService;
import com.ezen.valuefinder.service.WishService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequiredArgsConstructor
public class AuctionController {


    private final AuctionService auctionService;
    private final BiddingService biddingService;
    private final ReversebidService reversebidService;

    private final WishService wishService;

    private final CategoryService categoryService;

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

        Integer bidCount = auctionService.getBiddingCount(auction);
        model.addAttribute("bidCount",bidCount);

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

        return "auction/query/queryform";

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

                    if(price <= 0) {
                        return new ResponseEntity("0보다 큰 슷자를 입력해주세요.", HttpStatus.BAD_REQUEST);
                    }

                    // 비공개 경매라면
                    if (auction.getAuctionType() == AuctionType.SEALED) {
                        if(biddingService.chkSealedBidding(auction,principalDetails.getMember())) {
                            biddingService.chkStatus(auction);
                            biddingService.joinBidding(principalDetails.getUsername(), auctionNo, price);
                            return new ResponseEntity("입찰 완료", HttpStatus.OK);
                        } else {
                            return new ResponseEntity("비공개경매는 한번만 입찰 가능합니다.", HttpStatus.BAD_REQUEST);
                        }
                    }

                    // 비공개 경매가 아니라면
                    if (auction.getAuctionType() != AuctionType.SEALED) {

                        if (price <= auction.getAuctionNowPrice()) {
                            return new ResponseEntity("현재 금액보다 큰 금액을 입력해주세요.", HttpStatus.BAD_REQUEST);
                        }

                        if(biddingService.chkBidding(auction,principalDetails.getMember())) {
                            biddingService.chkStatus(auction);
                            biddingService.joinBidding(principalDetails.getUsername(), auctionNo, price);
                            return new ResponseEntity("입찰 완료", HttpStatus.OK);
                        }
                        return new ResponseEntity("현재 최상위 입찰자입니다.", HttpStatus.BAD_REQUEST);
                    }

                }
                case END -> {
                    return new ResponseEntity("종료된 경매입니다.", HttpStatus.LOCKED);
                }
            }
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @GetMapping(value = "/auction/report")
    public String reportAuction() {
        return "/auction/report";
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

    @PostMapping(value = "/auction/review/add/{auctionNo}")
    public String addAuctionReview(@PathVariable Long auctionNo, Authentication authentication, @Valid ReviewFormDto reviewFormDto, Model model
    , RedirectAttributes redirectAttributes) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        Member member = principalDetails.getMember();
        if(auctionService.chkAuctionReview(auctionNo)){
            auctionService.addAuctionReview(reviewFormDto,auctionNo,member);
            return "redirect:/member/mypage/successfulbid";
        } else {
            model.addAttribute("reviewFormDto", new ReviewFormDto());
            redirectAttributes.addFlashAttribute("errorMessage","이미 리뷰를 작성한 경매입니다.");
            return "redirect:/member/mypage/successfulbid";
        }
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
    public String auctionRealtime(@PathVariable("page") Optional<Integer> page, Model model,@RequestParam Long category) {
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 10);
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
    public String auctionSealedbid(@PathVariable("page") Optional<Integer> page, Model model,@RequestParam Long category) {
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 10);
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
    public String auctionPublicbid(@PathVariable("page") Optional<Integer> page, Model model,@RequestParam("category") Long category) {
        Page<Auction> auctionList;
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 10);

        auctionList = auctionService.getAuctionList(pageable, AuctionType.PUBLIC,category);


        for (Auction auction : auctionList) {
            auctionService.updateAuction(auction.getAuctionNo());
        }

        model.addAttribute("nowTime", LocalDateTime.now());
        model.addAttribute("auctionList", auctionList);
        model.addAttribute("maxPage", 5);

        return "auction/publicbid";
    }

    @GetMapping(value = {"/auction/search","/auction/search/{page}"})
    public String searchView(@PathVariable("page") Optional<Integer> page, Model model,@RequestParam("category") Long category) {
        Page<Auction> auctionList;
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 10);
        if(category != 0) {
            Category categorys = categoryService.getCategory(category);
            model.addAttribute("category",categorys);
        }

        auctionList = auctionService.getSearchList(pageable,category);


        for (Auction auction : auctionList) {
            auctionService.updateAuction(auction.getAuctionNo());
        }

        model.addAttribute("nowTime", LocalDateTime.now());
        model.addAttribute("auctionList", auctionList);
        model.addAttribute("maxPage", 5);

        return "auction/searchview";
    }

    //비공개 경매 페이지
    @GetMapping(value = "/auction/reversebid/details")
    public String redetails() {

        return "auction/reversebid/details";
    }

    @PostMapping(value = "/auction/searched")
    public String searchAuction(@Valid Long category, @Valid String searchVal,Model model,Optional<Integer> page) {
        Page<Auction> auctionList;
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 10);
        if(category != 0) {
            Category categorys = categoryService.getCategory(category);
            model.addAttribute("category",categorys);
        }
        auctionList = auctionService.getSearchValList(pageable,category,searchVal);
        for (Auction auction : auctionList) {
            auctionService.updateAuction(auction.getAuctionNo());
        }
        model.addAttribute("nowTime", LocalDateTime.now());
        model.addAttribute("auctionList", auctionList);
        model.addAttribute("maxPage", 5);

        return "auction/searchview";
    }

}

