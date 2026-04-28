package com.commerceapp.admin.dto;

import com.commerceapp.admin.entity.Admin;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class AdminPageResponse {

    private final List<AdminListResponse> content;
    private final int currentPage;
    private final int size;
    private final long totalElements;
    private final int totalPages;


    private AdminPageResponse(Page<Admin> adminPage) {

        this.content = adminPage.getContent().stream()
                .map(AdminListResponse::from)
                .collect(Collectors.toList());
        this.currentPage = adminPage.getNumber() + 1;
        this.size = adminPage.getSize();
        this.totalElements = adminPage.getTotalElements();
        this.totalPages = adminPage.getTotalPages();
    }

    public static AdminPageResponse from(Page<Admin> adminPage){
        return new AdminPageResponse(adminPage);
    }
}
