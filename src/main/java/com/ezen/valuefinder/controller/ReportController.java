package com.ezen.valuefinder.controller;

import com.ezen.valuefinder.config.PrincipalDetails;
import com.ezen.valuefinder.dto.AuctionReportDto;
import com.ezen.valuefinder.entity.Auction;
import com.ezen.valuefinder.entity.Member;
import com.ezen.valuefinder.service.AuctionReportService;
import com.ezen.valuefinder.service.AuctionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@RequiredArgsConstructor
@Controller
public class ReportController {

    private final AuctionService auctionService;
    private final AuctionReportService auctionReportService;

    //경매 신고 페이지 띄우기
    @GetMapping(value = "/auction/report/{auctionNo}")
    public String reportAuction(@PathVariable Long auctionNo,Model model, Authentication authentication) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        Member member = principalDetails.getMember();
        model.addAttribute("auctionReportDto", new AuctionReportDto());
        Auction auction = auctionService.findById(auctionNo);
        model.addAttribute("auction",auction); // 신고할 경매
        model.addAttribute("member",member); // 지금 접속한 사용자


        return "/auction/report";
    }

    //경매 신고글 등록하기
    @PostMapping(value = "/auction/report/{auctionNo}")
    public String addReportAuction(@Valid AuctionReportDto auctionReportDto, BindingResult bindingResult,
                                   Model model, Authentication authentication, @PathVariable Long auctionNo) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        Member member = principalDetails.getMember();
        Auction auction = auctionService.findById(auctionNo);
        if(bindingResult.hasErrors()) {
            model.addAttribute("auction",auction); // 신고할 경매
            model.addAttribute("member",member); // 지금 접속한 사용자
            //model.addAttribute("auctionReportDto", new AuctionReportDto());
            return "auction/report";
        }

        try {
            auctionReportService.saveReport(auctionReportDto, member, auction);
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("auction",auction); // 신고할 경매
            model.addAttribute("member",member); // 지금 접속한 사용자
            model.addAttribute("auctionReportDto", new AuctionReportDto());
            return "auction/report";
        }

        return "redirect:/";
    }
}
