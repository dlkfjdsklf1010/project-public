package com.commerceapp.customer.service;

import com.commerceapp.common.config.PasswordEncoder;
import com.commerceapp.customer.dto.LoginRequest;
import com.commerceapp.customer.entity.Customer;
import com.commerceapp.customer.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public Page<Customer> getCustomers(String keyword, String status, int page, int size, String sortBy, String sortOrder) {
        Sort sort = sortOrder.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page - 1, size, sort);
        return customerRepository.findByKeywordAndStatus(keyword, status, pageable);
    }

    /// 로그인
    @Transactional(readOnly = true)
    public Customer login(LoginRequest request) {
        Customer customer = customerRepository.findByEmail(request.getEmail()).orElseThrow(
                () -> new IllegalArgumentException("이메일 또는 비밀번호가 올바르지 않습니다.")
        );

        if (!passwordEncoder.matches(request.getPassword(), customer.getPassword())) {
            throw new IllegalArgumentException("이메일 또는 비밀번호가 올바르지 않습니다.");
        }

        return customer;
    }
}
