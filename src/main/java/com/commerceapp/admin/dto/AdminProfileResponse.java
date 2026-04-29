package com.commerceapp.admin.dto;

import com.commerceapp.admin.entity.Admin;
import lombok.Getter;

@Getter
public class AdminProfileResponse {

    private final String name;
    private final String email;
    private final String phoneNumber;

    private AdminProfileResponse(String name, String email, String phoneNumber) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public static AdminProfileResponse from(Admin admin){
        return new AdminProfileResponse(
        admin.getName(),
        admin.getEmail(),
        admin.getPhoneNumber()
        );
    }

}
