package com.commerceapp.customer.dto;

import com.commerceapp.customer.entity.Customer;
import lombok.Getter;

@Getter
public class CustomerDeleteResponse {
    private Long id;
    private String name;
    private String email;

    public CustomerDeleteResponse(Customer customer) {
        this.id = customer.getId();
        this.name = customer.getName();
        this.email = customer.getEmail();
    }

}
