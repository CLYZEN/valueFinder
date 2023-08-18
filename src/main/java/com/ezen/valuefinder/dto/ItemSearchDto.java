package com.ezen.valuefinder.dto;

import com.ezen.valuefinder.constant.AuctionType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemSearchDto {
	private String searchDateType;
	private AuctionType auctionType;
	private String searchBy;
	private String searchQuery = "";
}
