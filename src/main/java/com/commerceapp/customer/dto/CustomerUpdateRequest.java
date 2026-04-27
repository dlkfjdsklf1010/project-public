package com.commerceapp.customer.dto;

import lombok.Getter;

@Getter
public class CustomerUpdateRequest {
    private String name;
    private String email;
    private String phoneNumber;
}
