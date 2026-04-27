package com.commerceapp.customer.controller;

import com.commerceapp.customer.dto.*;
import com.commerceapp.customer.service.CustomerService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

/**
 * 고객 관리 Controller 클래스
 * 고객 관련 HTTP 요청을 처리하고 응답을 반환합니다.
 * 기본 URL: /api/customers
 */
@RestController /* @Controller + @ResponseBody, 모든 메서드 반환값을 JSON으로 변환 */
@RequestMapping("/api/customers")
public class CustomerController {

    /*========== 속성 ===========*/

    private final CustomerService customerService;

    /*========== 생성자 ===========*/

    /* 생성자 주입 방식으로 CustomerService 의존성 주입 */
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    /*========== 기능 ===========*/

    /**
     * 고객 목록 조회 (GET /api/customers)
     * 키워드 검색, 상태 필터, 페이징, 정렬을 지원합니다.
     * @param keyword 검색 키워드 (이름 또는 이메일, 기본값: 빈 문자열)
     * @param status 상태 필터 (ACTIVE/INACTIVE/SUSPENDED, 기본값: 전체 조회)
     * @param page 페이지 번호 (기본값: 1)
     * @param size 페이지당 개수 (기본값: 10)
     * @param sortBy 정렬 기준 (기본값: createdAt)
     * @param sortOrder 정렬 순서 (기본값: asc)
     * @return 페이징된 고객 목록
     */
    @GetMapping
    public Page<CustomerListResponse> getCustomers(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "") String status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "asc") String sortOrder) {
        return customerService.getCustomers(keyword, status, page, size, sortBy, sortOrder)
                .map(CustomerListResponse::new);
    }

    /**
     * 고객 단건 조회 (GET /api/customers/{id})
     * @param id 고객 고유 식별자
     * @return 고객 상세 정보
     */
    @GetMapping("/{id}")
    public CustomerDetailResponse getCustomer(@PathVariable Long id) {
        return new CustomerDetailResponse(customerService.getCustomer(id));
    }

    /**
     * 고객 정보 수정 (PATCH /api/customers/{id})
     * 수정 가능한 필드: 이름, 이메일, 전화번호
     * @param id 고객 고유 식별자
     * @param request 수정할 고객 정보 (name, email, phoneNumber)
     * @return 수정된 고객 상세 정보
     */
    @PatchMapping("/{id}")
    public CustomerDetailResponse updateCustomer(@PathVariable Long id, @RequestBody CustomerUpdateRequest request) {
        return new CustomerDetailResponse(customerService.updateCustomer(id, request.getName(), request.getEmail(), request.getPhoneNumber()));
    }

    /**
     * 고객 상태 변경 (PATCH /api/customers/{id}/status)
     * 상태값: ACTIVE(활성), INACTIVE(비활성), SUSPENDED(정지)
     * @param id 고객 고유 식별자
     * @param request 변경할 상태값 (status)
     * @return 상태가 변경된 고객 상세 정보
     */
    @PatchMapping("/{id}/status")
    public CustomerDetailResponse updateStatus(@PathVariable Long id, @RequestBody CustomerStatusRequest request) {
        return new CustomerDetailResponse(customerService.updateStatus(id, request.getStatus()));
    }

    /**
     * 고객 삭제 (DELETE /api/customers/{id})
     * 소프트 딜리트 방식으로 isDeleted 플래그를 true로 변경합니다.
     * @param id 고객 고유 식별자
     * @return 삭제된 고객 정보
     */
    @DeleteMapping("/{id}")
    public CustomerDeleteResponse deleteCustomer(@PathVariable Long id) {
        return new CustomerDeleteResponse(customerService.deleteCustomer(id));
    }
}

