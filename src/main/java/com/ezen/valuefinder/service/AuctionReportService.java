package com.ezen.valuefinder.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ezen.valuefinder.dto.AuctionReportDto;
import com.ezen.valuefinder.entity.AuctionReport;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class AuctionReportService{
	
	public Long saveReport(AuctionReportDto auctionReportDto) {
		AuctionReport auctionReport = AuctionReportDto.createReport(auctionReportDto);
		
		return AuctionReport.getAuctionReportNo();
	}
}
