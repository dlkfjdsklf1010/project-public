package com.commerceapp.admin.repository;

import com.commerceapp.admin.entity.Admin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {

    // 이메일 중복체크
    boolean existsByEmail(String email);

    // 로그인용 이메일 조회
    Optional<Admin> findByEmailAndIsDeletedFalse(String email);

    // 관리자 리스트 검색, 삭제된 계정은 제외
    @Query("""
            SELECT a FROM Admin a
            WHERE a.isDeleted = false
            AND (:keyword IS NULL OR a.name LIKE %:keyword% OR a.email LIKE %:keyword%)
            AND (:role IS NULL OR a.role = :role)
            AND (:status IS NULL OR a.status = :status)                                          \s
           """)
    Page<Admin> searchAdmins(
            @Param("keyword") String keyword,
            @Param("role") com.commerceapp.admin.enums.AdminRole role,
            @Param("status") com.commerceapp.admin.enums.AdminStatus status,
            Pageable pageable
    );

}
