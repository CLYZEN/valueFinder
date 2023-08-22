package com.ezen.valuefinder.dto;

import com.ezen.valuefinder.entity.Category;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReverseAuctionFormDto {
    @NotBlank(message = "희망 물품명을 입력해주세요.")
    private String reverseBiddingTitle;

    @NotBlank(message = "희망사항을 입력해주세요.")
    private String reverseBiddingDetail;

    @NotNull(message = "희망가격을 입력해주세요.")
    private Integer hopePrice;

    @NotNull(message = "마감기간을 입력해주세요.")
    private LocalDateTime reverseBiddingExpireDate;

    @NotNull(message = "카테고리를 입력해주세요")
    private Category category;

}
