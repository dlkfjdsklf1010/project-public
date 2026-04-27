package com.commerceapp.admin.dto;

import com.commerceapp.admin.enums.AdminRole;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class AdminRoleUpdateRequest {

    @NotNull(message = "변경할 역할을 입력하세요.")
    private AdminRole role;
}
