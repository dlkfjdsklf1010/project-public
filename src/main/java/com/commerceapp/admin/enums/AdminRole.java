package com.commerceapp.admin.enums;

import lombok.Getter;

@Getter
public enum AdminRole {
    CS("CS관리자"),
    MANAGER("운영관리자"),
    SUPER("슈퍼관리자");

    private final String displayName;

    AdminRole(String displayName) {
        this.displayName = displayName;
    }

}
