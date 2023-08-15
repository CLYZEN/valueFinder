package com.ezen.valuefinder.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class MemberModifyDto {
    @NotBlank
    private String name;

    @NotBlank
    private String nickName;

    @NotNull
    private Integer addressNo;

    @NotBlank
    private String address;

    @NotBlank
    private String addressDetail;

    @NotBlank
    private String phone;

    @NotNull
    private Long bankCode;

    @NotBlank
    private String bankAddress;

    private LocalDate birthday;
}
