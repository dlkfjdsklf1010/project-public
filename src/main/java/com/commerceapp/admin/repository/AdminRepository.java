package com.commerceapp.admin.repository;

import com.commerceapp.admin.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Long> {

    boolean existsByEmail(String email);
}
