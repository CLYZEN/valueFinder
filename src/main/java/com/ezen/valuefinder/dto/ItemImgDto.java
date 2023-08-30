package com.ezen.valuefinder.dto;

import org.modelmapper.ModelMapper;

import com.ezen.valuefinder.entity.ItemImg;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemImgDto {
    private String itemImageName;

    private String itemOriImageName;

    private String itemImageUrl;

    private String repImgYn;
    
    private static ModelMapper modelMapper = new ModelMapper();
    
    public static ItemImgDto of(ItemImg itemImg) {
    	return modelMapper.map(itemImg, ItemImgDto.class);
    }
}
