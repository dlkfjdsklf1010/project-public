package com.commerceapp.customer.dto;

import lombok.Getter;

/**
 * 고객 상태 변경 요청 DTO
 * 고객 상태 변경 API 요청 시 사용됩니다.
 * 상태값: ACTIVE(활성), INACTIVE(비활성), SUSPENDED(정지)
 */
@Getter
public class CustomerStatusRequest {
    private String status; /* 변경할 상태값 */
}
