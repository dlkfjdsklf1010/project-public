package com.commerceapp.customers.controller;

import com.commerceapp.customers.dto.CustomerDetailResponse;
import com.commerceapp.customers.dto.CustomerListResponse;
import com.commerceapp.customers.service.CustomerService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public Page<CustomerListResponse> getCustomers(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "") String status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "asc") String sortOrder)
    {
        return customerService.getCustomers(keyword, status, page, size, sortBy, sortOrder)
                .map(CustomerListResponse::new);

    }

    @GetMapping("/{id}")
    public CustomerDetailResponse getCustomer(@PathVariable Long id) {
        return new CustomerDetailResponse(customerService.getCustomer(id));
    }
}
