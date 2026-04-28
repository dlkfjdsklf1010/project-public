package com.commerceapp.customer.dto;

import com.commerceapp.customer.entity.Customer;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 고객 목록 조회 응답 DTO
 * 고객 목록 조회 시 각 고객의 기본 정보를 반환합니다.
 */
@Getter
public class CustomerListResponse {
    private Long id; /* 고객 고유 식별자 */
    private String name; /* 고객 이름 */
    private String email; /* 이메일 */
    private String phoneNumber; /* 전화번호 */
    private String status; /* 상태 (ACTIVE/INACTIVE/SUSPENDED) */
    private LocalDateTime createdAt; /* 가입일 */

    /* Customer 엔티티를 받아 필요한 필드만 DTO에 담음 */
    public CustomerListResponse(Customer customer) {
        this.id = customer.getId();
        this.name = customer.getName();
        this.email = customer.getEmail();
        this.phoneNumber = customer.getPhoneNumber();
        this.status = customer.getStatus();
        this.createdAt = customer.getCreatedAt();
    }
}
