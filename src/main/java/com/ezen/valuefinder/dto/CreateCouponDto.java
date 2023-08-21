package com.ezen.valuefinder.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CreateCouponDto {
    @NotBlank(message = "쿠폰 제목을 입력해주세요.")
    private String couponTitle;
    @NotBlank(message = "쿠폰 설명을 입력해주세요.")
    private String couponDetail;
    @NotNull(message = "쿠폰 할인금액을 입력해주세요.")
    private Long couponDiscount;
    @NotNull(message = "쿠폰 사용기간을 입력해주세요.")
    private LocalDate startDate;
    @NotNull(message = "쿠폰 사용기간을 입력해주세요.")
    private LocalDate expireDate;
}
