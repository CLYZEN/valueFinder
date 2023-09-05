
package com.ezen.valuefinder.controller;


import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;


import com.ezen.valuefinder.config.PrincipalDetails;
import com.ezen.valuefinder.constant.AuctionStatus;
import com.ezen.valuefinder.constant.AuctionType;
import com.ezen.valuefinder.dto.*;
import com.ezen.valuefinder.entity.*;
import com.ezen.valuefinder.repository.BiddingRepository;
import com.ezen.valuefinder.service.*;


import com.ezen.valuefinder.service.ReversebidService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


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


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequiredArgsConstructor
public class AuctionController {

    private final AuctionService auctionService;
    private final BiddingService biddingService;
    private final ReversebidService reversebidService;
    private final CategoryService categoryService;
    private final WishService wishService;
    private final AuctionQueryService auctionQueryService;
    private final AuctionReportService auctionReportService;


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

    @GetMapping(value = {"auction/detail/{auctionNo}", "auction/detail/{auctionNo}/{page}"})
    public String auctionDetail(Model model, @PathVariable("auctionNo") Long auctionNo, @PathVariable("page") Optional<Integer> page, Authentication authentication
            , HttpServletRequest request, HttpServletResponse response) {

        // 생성할 쿠키 이름
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

// 기존 auctionNo에 현재 auctionNo 추가
        if (existingAuctionNos == null || existingAuctionNos.isEmpty()) {
            existingAuctionNos = auctionNo.toString();
        } else {
            // 중복을 확인하고 중복된 값은 추가하지 않도록
            String[] auctionNosArray = existingAuctionNos.split("/");
            List<String> auctionNosList = new ArrayList<>(Arrays.asList(auctionNosArray));
            if (!auctionNosList.contains(auctionNo.toString())) {
                auctionNosList.add(auctionNo.toString());
            }
            existingAuctionNos = String.join("/", auctionNosList);
        }

// auctionNo 목록을 쿠키에 저장 (유효 기간 24시간)
        Cookie auctionNoCookie = new Cookie(cookieName, existingAuctionNos);
        auctionNoCookie.setMaxAge(24 * 60 * 60); // 24시간 (초 단위)
        auctionNoCookie.setPath("/"); // 쿠키의 경로 설정
        response.addCookie(auctionNoCookie);


        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        Auction auction = auctionService.getAuctionDetail(auctionNo);
        auctionService.addAuctionView(auctionNo);
        boolean checkWish = wishService.checkWish(auctionNo, principalDetails.getMember().getMemberId());
        model.addAttribute("checkWish", checkWish);
        model.addAttribute("auction", auction);
        model.addAttribute("nowTime", LocalDateTime.now());
        model.addAttribute("itemCount", auctionService.itemCount(auction.getItem().getMember().getMemberId()));
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 6);
        Page<Auction> auctionList = auctionService.getDetailPageAuctionList(auction.getItem().getMember(), pageable);
        model.addAttribute("auctionList", auctionList);
        auctionService.updateAuction(auctionNo);
        Page<AuctionReview> auctionReview = auctionService.getAuctionReviewList(auction.getItem().getMember().getMemberId(), pageable);
        model.addAttribute("auctionReview", auctionReview);
        model.addAttribute("reviewCount", auctionService.reviewCount(auction.getItem().getMember().getMemberId()));
        model.addAttribute("maxPage", 5);
        model.addAttribute("biddingList", biddingService.getBiddingList(pageable, auction));
        model.addAttribute("auctionQueryList", auctionQueryService.getAuctionQueryList(pageable, auction));
        model.addAttribute("remainingTime", auction.getRemainingTime());

        if (auction.getAuctionType() == AuctionType.PUBLIC) {
            return "/auction/details/publicDetail";
        } else if (auction.getAuctionType() == AuctionType.REALTIME) {
            return "/auction/details/realtimeDetail";
        } else {
            return "/auction/details/sealedDetail";
        }
    }

    @GetMapping(value = "/auction/reversebid/enter/add")
    public String enterQuery() {
        return "/auction/enter/enterForm";
    }

    @GetMapping(value = "/auction/report")
    public String reportAuction() {
        return "/auction/report";
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
    public String enterReversebid(Authentication authentication, Model model,
                                  @Valid ReversebidEnterDto reversebidEnterDto, @RequestParam("image") List<MultipartFile> itemImgFiles,
                                  @PathVariable Long bidno) {
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

    @GetMapping(value = "/auction/reversebid/enter/{bidno}")
    public String enterDetail(Model model, @PathVariable("bidno") Long bidno) {

        ReverseBiddingJoin reverseBiddingJoin = reversebidService.getReversebidJoinById(bidno);

        model.addAttribute("bid", reverseBiddingJoin);
        return "/auction/enter/enter";
    }

    @PostMapping(value = "/auction/review/add/{auctionNo}")
    public String addAuctionReview(@PathVariable Long auctionNo, Authentication authentication, @Valid ReviewFormDto reviewFormDto, Model model
            , RedirectAttributes redirectAttributes) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        Member member = principalDetails.getMember();
        if (auctionService.chkAuctionReview(auctionNo)) {
            auctionService.addAuctionReview(reviewFormDto, auctionNo, member);
            return "redirect:/member/mypage/successfulbid";
        } else {
            model.addAttribute("reviewFormDto", new ReviewFormDto());
            redirectAttributes.addFlashAttribute("errorMessage", "이미 리뷰를 작성한 경매입니다.");
            return "redirect:/member/mypage/successfulbid";
        }
    }


    // 실시간 경매 페이지
    @GetMapping(value = {"/auction/realtime", "/auction/realtime/{page}"})
    public String auctionRealtime(@PathVariable("page") Optional<Integer> page, Model model, @RequestParam Long category) {
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 10);
        Page<Auction> auctionList = auctionService.getAuctionList(pageable, AuctionType.REALTIME, category);

        for (Auction auction : auctionList) {
            auctionService.updateAuction(auction.getAuctionNo());
        }

        model.addAttribute("nowTime", LocalDateTime.now());
        model.addAttribute("auctionList", auctionList);
        model.addAttribute("maxPage", 5);
        return "auction/realtime";
    }

    //역경매
    @GetMapping(value = {"/auction/reversebid", "/auction/reversebid/{page}"})
    public String auctionReversebid(@PathVariable("page") Optional<Integer> page, Model model, @RequestParam Long category) {
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 10);
        Page<ReverseBidding> reverseAuctionList = auctionService.getReverseAuctionList(pageable, category);

        model.addAttribute("nowTime", LocalDateTime.now());
        model.addAttribute("reverseAuctionList", reverseAuctionList);
        model.addAttribute("maxPage", 5);
        return "auction/reversebid";
    }

    @GetMapping(value = {"/auction/reversebid/detail/{reverseBiddingNo}", "/auction/reversebid/detail/{reverseBiddingNo}/{page}"})
    public String redetails(Model model, @PathVariable("reverseBiddingNo") Long reverseBiddingNo,
                            @PathVariable("page") Optional<Integer> page) {
        ReverseBidding reverseBidding = reversebidService.getReversebidById(reverseBiddingNo);
        reversebidService.addReverseBiddingView(reverseBiddingNo);

        Page<ReverseBiddingJoin> reverseBiddingJoinList;
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 6);

        reverseBiddingJoinList = reversebidService.getReverseJoinList(pageable, reverseBiddingNo);

        model.addAttribute("nowTime", LocalDateTime.now());
        model.addAttribute("remainTime", auctionService.getRemainTime(reverseBidding.getReverseBiddingExpireDate()));
        model.addAttribute("reverseBidding", reverseBidding);
        reversebidService.updateAuctionStatusToEnd(reverseBiddingNo);
        model.addAttribute("reverseBiddingJoinList", reverseBiddingJoinList);
        model.addAttribute("maxPage", 5);
        Page<AuctionReview> auctionReview = auctionService.getAuctionReviewList(reverseBidding.getMember().getMemberId(), pageable);
        model.addAttribute("auctionReview", auctionReview);
        model.addAttribute("reviewCount", auctionService.reviewCount(reverseBidding.getMember().getMemberId()));
        return "auction/reversebid/details";
    }

    @GetMapping(value = {"/auction/sealedbid", "/auction/sealedbid/{page}"})
    public String auctionSealedbid(@PathVariable("page") Optional<Integer> page, Model model, @RequestParam Long category) {
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 10);
        Page<Auction> auctionList = auctionService.getAuctionList(pageable, AuctionType.SEALED, category);

        for (Auction auction : auctionList) {
            auctionService.updateAuction(auction.getAuctionNo());
        }


        model.addAttribute("nowTime", LocalDateTime.now());
        model.addAttribute("auctionList", auctionList);
        model.addAttribute("maxPage", 5);

        return "auction/sealedbid";
    }

    @GetMapping(value = {"/auction/publicbid", "/auction/publicbid/{page}"})
    public String auctionPublicbid(@PathVariable("page") Optional<Integer> page, Model model, @RequestParam("category") Long category) {
        Page<Auction> auctionList;
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 10);

        auctionList = auctionService.getAuctionList(pageable, AuctionType.PUBLIC, category);


        for (Auction auction : auctionList) {
            auctionService.updateAuction(auction.getAuctionNo());
        }

        model.addAttribute("nowTime", LocalDateTime.now());
        model.addAttribute("auctionList", auctionList);
        model.addAttribute("maxPage", 5);

        return "auction/publicbid";
    }


    @GetMapping(value = {"/auction/search", "/auction/search/{page}"})
    public String searchView(@PathVariable("page") Optional<Integer> page, Model model, @RequestParam("category") Long category, @RequestParam("searchVal") String searchVal) {
        Page<Auction> auctionList;
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 10);
        if (category != 0) {
            Category categorys = categoryService.getCategory(category);
            model.addAttribute("category", categorys);
        }
        auctionList = auctionService.getSearchValList(pageable, category, searchVal);
        for (Auction auction : auctionList) {
            auctionService.updateAuction(auction.getAuctionNo());
        }

        for (Auction auction : auctionList) {
            auctionService.updateAuction(auction.getAuctionNo());
        }
        model.addAttribute("nowTime", LocalDateTime.now());
        model.addAttribute("auctionList", auctionList);
        model.addAttribute("maxPage", 5);

        return "auction/searchview";
    }

}
