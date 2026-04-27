package com.commerceapp.customer.dto;

import com.commerceapp.customer.entity.Customer;
import lombok.Getter;

/**
 * 고객 삭제 응답 DTO
 * 고객 삭제 성공 시 삭제된 고객의 기본 정보를 반환합니다.
 */
@Getter
public class CustomerDeleteResponse {
    private Long id; /* 고객 고유 식별자 */
    private String name; /* 고객 이름 */
    private String email; /* 고객 이메일 */

    /* Customer 엔티티를 받아 필요한 필드만 DTO에 담음 */
    public CustomerDeleteResponse(Customer customer) {
        this.id = customer.getId();
        this.name = customer.getName();
        this.email = customer.getEmail();
    }

}
