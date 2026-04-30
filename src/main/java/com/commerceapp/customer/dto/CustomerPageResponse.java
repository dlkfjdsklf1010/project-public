package com.commerceapp.customer.dto;

import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
public class CustomerPageResponse {
    private List<CustomerListResponse> customerList;
    private int currentPage;
    private long totalCount;
    private int totalPages;

    public CustomerPageResponse(Page<CustomerListResponse> page) {
        this.customerList = page.getContent();
        this.currentPage = page.getNumber() + 1;
        this.totalCount = page.getTotalElements();
        this.totalPages = page.getTotalPages();
    }


}
