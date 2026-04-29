package com.commerceapp.admin.dto;

import com.commerceapp.admin.entity.Admin;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class AdminListResponse {

    private final Long id;
    private final String name;
    private final String email;
    private final String phoneNumber;
    private final String role;
    private final String status;
    private final LocalDateTime createdAt;
    private final LocalDateTime approvedAt;

    private AdminListResponse(Long id, String name, String email, String phoneNumber, String role, String status, LocalDateTime createdAt, LocalDateTime approvedAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.status = status;
        this.createdAt = createdAt;
        this.approvedAt = approvedAt;
    }

    public static AdminListResponse from(Admin admin){
        return new AdminListResponse(
                admin.getId(),
                admin.getName(),
                admin.getEmail(),
                admin.getPhoneNumber(),
                admin.getRole(),
                admin.getStatus(),
                admin.getCreatedAt(),
                admin.getApprovedAt()
        );
    }
}
