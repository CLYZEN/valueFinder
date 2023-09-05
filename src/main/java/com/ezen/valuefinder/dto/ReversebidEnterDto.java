package com.ezen.valuefinder.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;

import com.ezen.valuefinder.entity.Item;
import com.ezen.valuefinder.entity.Member;
import com.ezen.valuefinder.entity.ReverseBiddingJoin;

@Getter
@Setter
public class ReversebidEnterDto {
    private List<ItemImgDto> itemImgDtoList;

    
    private Long reverseBiddingJoinNo;
    
    private String nickname; // 닉네임
    
    private String itemDetail; // 물품설명
    
    private String itemName; // 물품명
    
    private Integer suggestPrice;
    
    private String email; // 이메일(아이디)
    
    private ReverseBiddingJoin reverseBiddingJoin;
    private Member member;    
    private Item item;
    
    
    private static ModelMapper modelMapper = new ModelMapper();
    
    
    
    public ReverseBiddingJoin createReverseBidding() {
    	return modelMapper.map(this, ReverseBiddingJoin.class);
    }

    public static ReversebidEnterDto of(ReverseBiddingJoin reverseBiddingJoin) {
    	return modelMapper.map(reverseBiddingJoin,ReversebidEnterDto.class);
    }
}
