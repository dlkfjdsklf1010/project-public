package com.commerceapp.admin.enums;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum AdminStatus {

    ACTIVATE("활성",null),
    PENDING("승인대기","계정 승인대기 중"),
    REJECTED("거부","계정 신청 거부됨"),
    BANNED("정지","계정 정지됨"),
    DEACTIVATE("비활성","계정 비활성화됨");

    private final String databaseValue;
    private final String message;

    AdminStatus(String databaseValue, String message) {
        this.databaseValue = databaseValue;
        this.message = message;
    }

    public static AdminStatus from(String databaseValue){
        return Arrays.stream(values())
                .filter(status -> status.databaseValue.equals(databaseValue))
                .findFirst()
                .orElse(null);
    }

    public boolean isLogin(){
        return this == ACTIVATE;
    }

}
