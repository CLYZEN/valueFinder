package com.ezen.valuefinder.dto;

import jakarta.validation.Valid;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class ModifyPasswordDto {

    @Length(min = 8, max = 15, message = "8자 ~ 15자 이내로 작성해주세요,")
    private String nowPassword;
    @Length(min = 8, max = 15, message = "8자 ~ 15자 이내로 작성해주세요,")
    private String updatePassword;
    @Length(min = 8, max = 15, message = "8자 ~ 15자 이내로 작성해주세요,")
    private String confirmPassword;

}
