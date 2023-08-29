package com.ezen.valuefinder.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ezen.valuefinder.dto.AuctionReportDto;
import com.ezen.valuefinder.entity.Auction;
import com.ezen.valuefinder.entity.AuctionReport;
import com.ezen.valuefinder.entity.Member;
import com.ezen.valuefinder.repository.AuctionReportRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class AuctionReportService{
	
	private final AuctionReportRepository auctionReportRepository;
	
	public void saveReport(AuctionReportDto auctionReportDto, Member member, Auction auction) {
		AuctionReport auctionReport = new AuctionReport();
		auctionReport.setAuctionReportTitle(auctionReportDto.getAuctionReportTitle());
		auctionReport.setAuctionReportDetail(auctionReportDto.getAuctionReportTitle());
		auctionReport.setAuction(auction);
		auctionReport.setMember(member);
		
	}
}
