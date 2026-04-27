package com.commerceapp.customers.service;

import com.commerceapp.customers.entity.Customers;
import com.commerceapp.customers.repository.CustomerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Transactional(readOnly = true)
    public Page<Customers> getCustomers(String keyword, String status, int page, int size, String sortBy, String sortOrder) {
        Sort sort = sortOrder.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page - 1, size, sort);
        return customerRepository.findByKeywordAndStatus(keyword, status, pageable);
    }

    @Transactional(readOnly = true)
    public Customers getCustomer(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 고객이 존재하지 않습니다."));
    }

    @Transactional
    public Customers updateCustomer(Long id, String name, String email, String phoneNumber) {
        Customers customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 고객이 존재하지 않습니다."));
        customer.updateCustomers(name, email, phoneNumber);
        return customer;
    }

    @Transactional
    public Customers updateStatus(Long id, String status) {
        Customers customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 고객이 존재하지 않습니다."));
        customer.updateStatus(status);
        return customer;
    }
}
