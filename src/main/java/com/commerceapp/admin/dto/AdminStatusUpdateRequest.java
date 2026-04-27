package com.commerceapp.admin.dto;

import com.commerceapp.admin.enums.AdminStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class AdminStatusUpdateRequest {

    @NotNull(message = "변경할 상태를 입력하세요.")
    private AdminStatus status;
}
