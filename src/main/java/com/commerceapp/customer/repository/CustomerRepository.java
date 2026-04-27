package com.commerceapp.customer.repository;

import com.commerceapp.customer.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    /* 이메일 중복 확인 */
    boolean existsByEmail(String email);


    @Query("SELECT c FROM Customer c WHERE (c.name LIKE %:keyword% OR c.email LIKE %:keyword%) AND (:status IS NULL OR c.status = :status)")
    Page<Customer> findByKeywordAndStatus(@Param("keyword") String keyword, @Param("status") String status, Pageable pageable);

    /// customer에서 email을 찾는 메서드, 예외 처리를 위해 Optional 사용
    Optional<Customer> findByEmail(String email);
}

