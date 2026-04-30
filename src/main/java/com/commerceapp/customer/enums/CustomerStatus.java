package com.commerceapp.customer.enums;

import lombok.Getter;

@Getter
public enum CustomerStatus {
    ACTIVE("활성"), /* 활성 */
    INACTIVE("비활성"), /* 비활성 */
    SUSPENDED("정지") /* 정지 */;

    private final String databaseValue;

    CustomerStatus(String databaseValue) {
        this.databaseValue = databaseValue;
    }
}
