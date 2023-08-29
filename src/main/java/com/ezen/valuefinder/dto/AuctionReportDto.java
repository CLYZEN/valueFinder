package com.ezen.valuefinder.dto;

import org.modelmapper.ModelMapper;

import com.ezen.valuefinder.entity.AuctionReport;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuctionReportDto {
	@NotBlank(message = "제목을 입력해주세요.")
	private String auctionReportTitle; // 경매신고제목
	 
	@NotBlank(message = "신고내용을 입력해주세요.") 
	private String auctionReportDetail; // 경매신고내용
	
	private static ModelMapper modelMapper = new ModelMapper();
	
	public AuctionReport createReport() {
		return modelMapper.map(this,AuctionReport.class);
	}
}
