package com.ezen.valuefinder.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuctionReportDto {
	@NotBlank(message = "제목을 입력해주세요.")
	private String auctionReportTitle; // 경매신고제목
	 
	@NotBlank(message = "내용을 입력해주세요.") 
	private String auctionReportDetail; // 경매신고내용
	
	
}
