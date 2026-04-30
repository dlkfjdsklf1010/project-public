package com.commerceapp.order.enums;

import lombok.Getter;

@Getter
public enum OrderStatus {
    READY("준비중"),
    SHIPPING("배송중"),
    COMPLETED("배송완료"),
    CANCELED("취소됨");

    private final String displayName;

    OrderStatus(String displayName) {
        this.displayName = displayName;
    }
}
