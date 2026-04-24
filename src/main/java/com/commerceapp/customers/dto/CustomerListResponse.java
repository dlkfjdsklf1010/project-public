package com.commerceapp.customers.dto;

import com.commerceapp.customers.entity.Customers;
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

    public CustomerListResponse(Customers  customers) {
        this.id = customers.getId();
        this.name = customers.getName();
        this.email = customers.getEmail();
        this.phoneNumber = customers.getPhoneNumber();
        this.status = customers.getStatus();
        this.createdAt = customers.getCreatedAt();
    }
}
