package com.commerceapp.customer.dto;

import com.commerceapp.customer.entity.Customer;
import lombok.Getter;

@Getter
public class CustomerLoginSession {
    private final Long customerId;
    private final String email;

    private CustomerLoginSession(Long customerId, String email) {
        this.customerId = customerId;
        this.email = email;
    }

    public static CustomerLoginSession from(Customer customer) {
        return new CustomerLoginSession(
                customer.getId(),
                customer.getEmail()
        );
    }
}
