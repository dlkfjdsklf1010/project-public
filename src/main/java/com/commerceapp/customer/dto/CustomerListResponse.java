package com.commerceapp.customer.dto;

import com.commerceapp.customer.entity.Customer;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CustomerListResponse {
    private Long id;
    private String name;
    private String email;
    private String phoneNumber;
    private String status;
    private LocalDateTime createdAt;

    public CustomerListResponse(Customer customer) {
        this.id = customer.getId();
        this.name = customer.getName();
        this.email = customer.getEmail();
        this.phoneNumber = customer.getPhoneNumber();
        this.status = customer.getStatus();
        this.createdAt = customer.getCreatedAt();
    }
}
