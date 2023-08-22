
package com.ezen.valuefinder.controller;

import com.ezen.valuefinder.config.PrincipalDetails;
import com.ezen.valuefinder.constant.AuctionStatus;
import com.ezen.valuefinder.constant.AuctionType;
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


    @PostMapping(value = "/auction/query/add")
    public String addQuery(@Valid AuctionQueryDto auctionQueryDto, Principal principal, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("auctionQueryDto", auctionQueryDto);
            return "/auction/query/add";
        }


        try {
            auctionService.createdQuery(auctionQueryDto, principal.getName());
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("auctionQueryDto", new AuctionQueryDto());

            return "/auction/query/add";
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
    public @ResponseBody ResponseEntity bidding(@RequestBody Map<String, Object> requestBody, Authentication authentication) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

        try {
            Long auctionNo = Long.parseLong(requestBody.get("auctionNo").toString());
            Long price = Long.parseLong(requestBody.get("price").toString());

            Auction auction = auctionService.getAuction(auctionNo);

            switch (auction.getAuctionStatus()) {
                case PENDING -> {
                    return  new ResponseEntity("경매 시작 전입니다.", HttpStatus.LOCKED);
                } case PROGRESS -> {
                    if(price <= auction.getAuctionNowPrice()) {
                        return new ResponseEntity("현재 금액보다 큰 금액을 입력해주세요.", HttpStatus.BAD_REQUEST);
                    }
                    biddingService.chkStatus(auction);
                    return new ResponseEntity("등록 완료", HttpStatus.OK);
                } case END -> {
                    return new ResponseEntity("종료된 경매입니다.", HttpStatus.LOCKED);
                } case LAST -> {
                    if(price <= auction.getAuctionNowPrice()) {
                        return new ResponseEntity("현재 금액보다 큰 금액을 입력해주세요.", HttpStatus.BAD_REQUEST);
                    }
                    biddingService.chkStatus(auction);
                    return new ResponseEntity("등록 완료",HttpStatus.OK);
                }
            }

            biddingService.joinBidding(principalDetails.getUsername(), auctionNo, price);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(HttpStatus.OK);
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


    //실시간 경매 페이지
    @GetMapping(value = {"/auction/realtime","/auction/realtime/{page}"})
    public String auctionRealtime(Optional<Integer> page,Model model) {
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0 , 30);
        Page<Auction> auctionList = auctionService.getAuctionList(pageable,AuctionType.REALTIME);
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
        model.addAttribute("nowTime",LocalDateTime.now());
        model.addAttribute("auctionList", auctionList);
        model.addAttribute("maxPage",5);
        return "auction/realtime";
    }

    //역경매
    @GetMapping(value = "/auction/reversebid")
    public String auctionReversebid() {

        return "auction/reversebid";
    }

    @GetMapping(value = {"/auction/sealedbid","/auction/sealedbid/{page}"})
    public String auctionSealedbid(Optional<Integer> page,Model model) {
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0 , 30);
        Page<Auction> auctionList = auctionService.getAuctionList(pageable,AuctionType.SEALED);
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
        model.addAttribute("nowTime",LocalDateTime.now());
        model.addAttribute("auctionList", auctionList);
        model.addAttribute("maxPage",5);

        return "auction/sealedbid";
    }

    //비공개 경매 페이지
    @GetMapping(value = "/auction/reversebid/details")
    public String redetails() {

        return "auction/reversebid/details";
    }

}
