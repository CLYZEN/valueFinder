package com.ezen.valuefinder.dto;




import lombok.Getter;
import lombok.Setter;


import jakarta.validation.constraints.NotNull;

@Getter
@Setter
public class AuctionQueryDto {
	
	
	private Integer auctionQueryDistinction;
	
	@NotNull(message = "제목 입력은 필수 입니다.")
	private String auctionQueryTitle;
	
	@NotNull(message = "내용을 입력해 주세요")
	private String auctionQueryDtail;
	
	
	
}
