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
    public CustomerListResponse(Long id, String name, String email, String phoneNumber, String status, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.status = status;
        this.createdAt = createdAt;
    }

    public static CustomerListResponse from(Customer customer) {
        return new CustomerListResponse(
                customer.getId(),
                customer.getName(),
                customer.getEmail(),
                customer.getPhoneNumber(),
                customer.getStatus().name(),
                customer.getCreatedAt()
        );
    }
}

