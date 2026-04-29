package com.commerceapp.product.enums;

import lombok.Getter;

@Getter
public enum ProductStatus {
    ON_SALE("판매중"),
    SOLD_OUT("품절"),
    DISCONTINUED("단종");

    private final String displayName;

    ProductStatus(String displayName) {
        this.displayName = displayName;
    }
}
