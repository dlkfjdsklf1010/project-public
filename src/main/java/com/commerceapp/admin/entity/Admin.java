package com.commerceapp.admin.entity;

import com.commerceapp.admin.enums.AdminRole;
import com.commerceapp.admin.enums.AdminStatus;
import com.commerceapp.common.entity.BaseEntity;
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
public class Admin extends BaseEntity {

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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AdminRole role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AdminStatus status;

    @Column
    private LocalDateTime approvedAt;

    @Column
    private LocalDateTime rejectedAt;

    @Column
    private String rejectReason;

    @Column(nullable = false)
    private Boolean isDeleted;

    public Admin(String name, String email, String password, String phoneNumber, AdminRole role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.status = AdminStatus.PENDING;
        this.isDeleted = false;
    }

    public void update(String name, String email, String phoneNumber) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public void updateMyPassword(String password){
        this.password = password;
    }

    public void activate(LocalDateTime currentTime) {
        this.status = AdminStatus.ACTIVATE;
        this.approvedAt = currentTime;
    }

    public void deactivate() {
        this.status = AdminStatus.DEACTIVATE;
    }

    public void ban() {
        this.status = AdminStatus.BANNED;
    }

    public void reject(String reason, LocalDateTime currentTime) {
        this.status = AdminStatus.REJECTED;
        this.rejectReason = reason;
        this.rejectedAt = currentTime;
    }

    public void changeRole(AdminRole newRole){
        this.role = newRole;
    }

    public void deleteAdmin(){
        this.isDeleted = true;
    }

}
