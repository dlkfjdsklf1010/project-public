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

    public CustomerListResponse(Customer customers) {
        this.id = customers.getId();
        this.name = customers.getName();
        this.email = customers.getEmail();
        this.phoneNumber = customers.getPhoneNumber();
        this.status = customers.getStatus();
        this.createdAt = customers.getCreatedAt();
    }
}
