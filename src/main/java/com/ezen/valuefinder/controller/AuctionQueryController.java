package com.ezen.valuefinder.controller;

import com.ezen.valuefinder.config.PrincipalDetails;
import com.ezen.valuefinder.dto.AuctionQueryDto;
import com.ezen.valuefinder.entity.Auction;
import com.ezen.valuefinder.entity.AuctionQuery;
import com.ezen.valuefinder.entity.Member;
import com.ezen.valuefinder.service.AuctionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class AuctionQueryController {

    private final AuctionService auctionService;

    @PostMapping(value = "/auction/query/add/{auctionNo}")
    public String addQuery(@Valid AuctionQueryDto auctionQueryDto, Authentication authentication,
                           BindingResult bindingResult, Model model, @PathVariable("auctionNo") Long auctionNo) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

        if (bindingResult.hasErrors()) {

            model.addAttribute("auctionQueryDto", auctionQueryDto);
            return "auction/query/add/" + auctionNo;
        }

        try {
            auctionService.createdQuery(auctionQueryDto, principalDetails.getUsername(), auctionNo);
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("auctionQueryDto", new AuctionQueryDto());

            return "auction/query/add/" + auctionNo;
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

        return "auction/query/query";
    }

    @DeleteMapping("/auction/query/{auctionQueryNo}/delete")
    public @ResponseBody ResponseEntity<Long> deleteQuery(@PathVariable("auctionQueryNo") Long auctionQueryNo
            , Authentication authentication) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();


        auctionService.deleteQuery(auctionQueryNo);

        return new ResponseEntity<Long>(auctionQueryNo , HttpStatus.OK);
    }

    @GetMapping(value = "/auction/query")
    public String queryDetail() {
        return "auction/query/query";
    }

}
