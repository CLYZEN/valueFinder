package com.ezen.valuefinder.dto;




import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import org.modelmapper.ModelMapper;

import com.ezen.valuefinder.constant.AuctionQueryDistinction;
import com.ezen.valuefinder.entity.AuctionQuery;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Getter
@Setter
public class AuctionQueryDto {


	private Integer auctionQueryDistinction;

	@NotNull(message = "제목 입력은 필수 입니다.")
	private String auctionQueryTitle;
<<<<<<< HEAD
	
=======

>>>>>>> 44837fdc17c9981f921e50a4e7b6e8167d2a664e
	@NotBlank(message = "내용을 입력해 주세요")
	private String auctionQueryDetail;
	
	

	}

