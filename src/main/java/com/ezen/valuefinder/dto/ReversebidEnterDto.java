package com.ezen.valuefinder.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;

import com.ezen.valuefinder.entity.Item;
import com.ezen.valuefinder.entity.Member;
import com.ezen.valuefinder.entity.ReverseBidding;
import com.ezen.valuefinder.entity.ReverseBiddingJoin;

@Getter
@Setter
public class ReversebidEnterDto {
    private String title;
    private Integer price;
    private String detail;
    private List<ItemImgDto> itemImgDtoList = new ArrayList<>();

    private Long reverseBiddingJoinNo;
    
    private ReverseBidding reverseBidding; 
    
    private Member member;
    
    private Item item;
    
    private String nickname; // 닉네임
    
    private Integer suggestPrice;
    
    private static ModelMapper modelMapper = new ModelMapper();
    
    public static ReversebidEnterDto of(ReverseBiddingJoin reverseBiddingJoin) {
    	return modelMapper.map(reverseBiddingJoin,ReversebidEnterDto.class);
    }
    
    public static ReversebidEnterDto of(Member member) {
    	return modelMapper.map(member,ReversebidEnterDto.class);
    }
}
