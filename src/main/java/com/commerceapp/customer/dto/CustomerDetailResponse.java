package com.commerceapp.customer.dto;

import com.commerceapp.customer.entity.Customer;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 고객 상세 조회 응답 DTO
 * 고객 상세 조회, 정보 수정, 상태 변경 시 응답으로 반환됩니다.
 */
@Getter
public class CustomerDetailResponse {

    private Long id; /* 고객 고유 식별자 */
    private String name; /* 고객 이름 */
    private String email; /* 이메일 */
    private String phoneNumber; /* 전화번호 */
    private String status; /* 상태 (ACTIVE/INACTIVE/SUSPENDED) */
    private LocalDateTime createdAt; /* 가입일 */

    /* Customer 엔티티를 받아 필요한 필드만 DTO에 담음 */
    public CustomerDetailResponse(Customer customers) {
        this.id = customers.getId();
        this.name = customers.getName();
        this.email = customers.getEmail();
        this.phoneNumber = customers.getPhoneNumber();
        this.status = customers.getStatus();
        this.createdAt = customers.getCreatedAt();
    }
}
