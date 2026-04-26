package com.commerceapp.customer.controller;

import com.commerceapp.customer.dto.LoginRequest;
import com.commerceapp.customer.dto.LoginResponse;
import com.commerceapp.customer.entity.Customer;
import com.commerceapp.customer.service.CustomerService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/")
public class CustomerController {
    private final CustomerService customerService;

    /// 로그인 (주문 생성 확인용으로 임의작성했으니 지우셔도 됩니다!)
    @PostMapping("/customers/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody LoginRequest request,
            HttpSession session
    ) {
        Customer customer = customerService.login(request);
        session.setAttribute("loginCustomer", customer);

        return ResponseEntity.ok(new LoginResponse(customer.getId()));
    }
}
