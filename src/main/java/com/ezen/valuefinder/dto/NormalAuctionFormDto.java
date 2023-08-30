package com.ezen.valuefinder.dto;

import com.ezen.valuefinder.entity.Auction;
import com.ezen.valuefinder.entity.Category;
import com.ezen.valuefinder.entity.ItemImg;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.ui.ModelMap;

@Getter
@Setter
public class NormalAuctionFormDto {

    @NotNull(message = "카테고리를 선택해주세요.")
    private Long category;

    @NotBlank(message = "물품 이름을 입력해주세요.")
    private String itemName;

    @NotBlank(message = "물품 설명을 입력해주세요.")
    private String itemDetail;

    private Integer itemWidth;

    private Integer itemDepth;

    private Integer itemHeight;

    @NotNull(message = "경매구분을 선택해주세요.")
    private Integer auctionDistinction;

    @NotNull(message = "경매 시작가를 입력해주세요.")
    private Long auctionStartPrice;

    @NotNull(message = "경매 시작 일자를 입력해주세요.")
    private LocalDateTime auctionStartTime;

    private LocalDateTime auctionEndTime;

    private List<ItemImgDto> itemImgDtoList = new ArrayList<>();
    
    private static ModelMapper modelMapper = new ModelMapper();
    
    public static NormalAuctionFormDto of(Auction auction) {
        NormalAuctionFormDto dto = modelMapper.map(auction, NormalAuctionFormDto.class);
        
        List<ItemImgDto> itemImgDtoList = new ArrayList<>();
        for (ItemImg itemImg : auction.getItem().getItemImgList()) {
            itemImgDtoList.add(ItemImgDto.of(itemImg));
        }
        dto.setItemImgDtoList(itemImgDtoList);
        
        return dto;
    }
    
}

