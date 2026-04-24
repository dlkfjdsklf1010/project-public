package com.commerceapp.admin.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "admins")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 20)
    private String name;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false, length = 20)
    @Pattern(regexp = "^010-\\d{4}-\\d{4}$")
    private String phoneNumber;
    @Column(nullable = false, length = 20)
    private String role;
    @Column(nullable = false, length = 20)
    private String status;
    @Column
    private LocalDateTime approvedAt;
    @Column
    private LocalDateTime rejectedAt;
    @Column
    private String rejectReason;
    @Column(nullable = false)
    private Boolean isDeleted;

    public Admin(String name, String email, String password, String phoneNumber, String role, String status){
        this.name = name;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.status = "승인대기";
        this.isDeleted = false;
    }

    public void activate() {
        this.status = "활성";
        this.approvedAt = currentTime;
    }

    public void deactivate() {
        this.status = "비활성";
    }

    public void ban() {
        this.status = "정지";
    }

    public void reject() {
        this.status = "거부";
        this.rejectedAt = currentTime;
        this.rejectReason = reason;
    }


}
