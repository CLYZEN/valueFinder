package com.ezen.valuefinder.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ReversebidEnterDto {
    private String title;
    private Integer price;
    private String detail;
    private List<ItemImgDto> itemImgDtoList = new ArrayList<>();
}
