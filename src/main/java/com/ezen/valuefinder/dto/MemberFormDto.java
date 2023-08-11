package com.ezen.valuefinder.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

@Getter
@Setter
public class MemberFormDto {

    @NotBlank(message = "아이디를 입력해주세요.")
    @Email(message = "이메일 형식으로 입력해주세요.")
    private String email;

    @NotBlank(message = "이름을 입력해주세요.")
    private String name;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Length(min = 8, max = 16, message = "비밀번호는 8자~16자 사이로 입력해주세요.")
    private String password;

    @NotNull(message = "주소를 입력해주세요.")
    private Integer addressNo;

    @NotBlank(message = "주소를 입력해주세요.")
    private String address;

    @NotBlank(message = "주소를 입력해주세요.")
    private String addressDetail;

    @NotBlank(message = "닉네임을 입력해주세요.")
    private String nickname;

    @NotBlank(message = "휴대폰번호를 입력해주세요.")
    private String phone;

    @NotNull(message = "은행을 선택해주세요.")
    private Long bankCode;

    @NotBlank(message = "계좌번호를 입력해주세요.")
    private String bankAddress;

    private LocalDate birthday;
}
