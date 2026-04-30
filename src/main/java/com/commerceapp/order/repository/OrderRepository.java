package com.commerceapp.order.repository;

import com.commerceapp.order.entity.Order;
import com.commerceapp.order.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface OrderRepository extends JpaRepository<Order, Long> {
    // 상품 리스트 조회
    @Query("""
        SELECT DISTINCT o FROM Order o
        JOIN o.customer c
        WHERE (:status IS NULL OR o.status = :status)
        AND (
            :keyword IS NULL OR
            o.orderNumber LIKE %:keyword% OR
            c.name LIKE %:keyword%
        )
    """)
    Page<Order> search(
            @Param("keyword") String keyword,
            @Param("status") OrderStatus status,
            Pageable pageable
    );

    // 주문 번호 자동 생성
    @Query("SELECT COUNT(o) FROM Order o WHERE o.createdAt BETWEEN :start AND :end")
    long countOrdersBetween(
            LocalDateTime start,
            LocalDateTime end
    );
}
