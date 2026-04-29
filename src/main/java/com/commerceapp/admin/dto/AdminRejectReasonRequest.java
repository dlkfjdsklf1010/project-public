package com.commerceapp.admin.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class AdminRejectReasonRequest {

    @NotBlank(message = "거부 사유는 필수 입력입니다")
    private String rejectReason;

}
