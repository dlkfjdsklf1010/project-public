package com.commerceapp.admin.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class AdminUpdateRequest {

    @NotBlank(message = "이름은 필수 입력입니다.")
    private String name;

    @NotBlank(message = "이메일은 필수 입력입니다.")
    @Email(message = "올바른 이메일 형식을 입력해주세요.")
    private String email;

    @NotBlank(message = "전화번호는 필수 입력입니다.")
    @Pattern(regexp = "^010-\\d{4}-\\d{4}$", message = "올바른 전화번호 형식이 아닙니다: 010-XXXX-XXXX")
    private String phoneNumber;

}
