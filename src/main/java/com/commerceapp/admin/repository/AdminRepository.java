package com.commerceapp.admin.repository;

import com.commerceapp.admin.entity.Admin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {

    boolean existsByEmail(String email);

    Optional<Admin> findByEmail(String email);

    @Query("""
            SELECT a FROM Admin a
            WHERE a.isDeleted = false
            AND (:keyword IS NULL OR a.name LIKE %:keyword% OR a.email LIKE %:keyword%)
            AND (:role IS NULL OR a.role = :role)
            AND (:status IS NULL OR a.status = :status)                                          \s
           """)
    Page<Admin> searchAdmins(
            @Param("keyword") String keyword,
            @Param("role") String role,
            @Param("status") String status,
            Pageable pageable
    );
}
