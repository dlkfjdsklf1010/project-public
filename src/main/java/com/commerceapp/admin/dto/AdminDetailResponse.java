package com.commerceapp.admin.dto;

import com.commerceapp.admin.entity.Admin;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@JsonPropertyOrder({"name","email","phoneNumber","role", "status", "createdAt", "approvedAt"})
public class AdminDetailResponse {

    private final String name;
    private final String email;
    private final String phoneNumber;
    private final String role;
    private final String status;
    private final LocalDateTime createdAt;
    private final LocalDateTime approvedAt;

    private AdminDetailResponse(String name, String email, String phoneNumber, String role, String status, LocalDateTime createdAt, LocalDateTime approvedAt) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.status = status;
        this.createdAt = createdAt;
        this.approvedAt = approvedAt;
    }

    public static AdminDetailResponse from(Admin admin){
        return new AdminDetailResponse(
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
