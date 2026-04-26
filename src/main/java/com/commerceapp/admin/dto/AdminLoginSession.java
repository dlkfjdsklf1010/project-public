package com.commerceapp.admin.dto;

import com.commerceapp.admin.entity.Admin;
import lombok.Getter;

@Getter
public class AdminLoginSession {

    private final Long id;
    private final String email;
    private final String role;

    private AdminLoginSession(Long id, String email, String role) {
        this.id = id;
        this.email = email;
        this.role = role;
    }

    public static AdminLoginSession from(Admin admin){
        return new AdminLoginSession(
                admin.getId(),
                admin.getEmail(),
                admin.getRole()
        );
    }
}
