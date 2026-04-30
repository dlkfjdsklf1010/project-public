package com.commerceapp.customer.repository;

import com.commerceapp.customer.enums.CustomerStatus;
import com.commerceapp.customer.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


/**
 * 고객 Repository 인터페이스
 * JpaRepository를 상속받아 기본 CRUD 메서드를 자동으로 제공받습니다.
 * Customer 엔티티와 Long 타입의 PK를 사용합니다.
 */
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    /**
     * 이메일 중복 확인
     * 회원가입 시 동일한 이메일이 존재하는지 확인합니다.
     * @param email 확인할 이메일
     * @return 존재하면 true, 없으면 false
     */
    boolean existsByEmail(String email);

    /**
     * 고객 목록 조회 (검색 + 상태 필터 + 페이징)
     * - 이름 또는 이메일로 키워드 검색
     * - 상태 필터 적용 (null이면 전체 조회)
     * - 소프트 딜리트된 고객(isDeleted = true) 제외
     * @param keyword 검색 키워드 (이름 또는 이메일)
     * @param status 상태 필터 (ACTIVE/INACTIVE/SUSPENDED, null이면 전체)
     * @param pageable 페이징 및 정렬 정보
     * @return 페이징된 고객 목록
     */
    @Query("SELECT c FROM Customer c WHERE (c.name LIKE %:keyword% OR c.email LIKE %:keyword%) AND (:status IS NULL OR c.status = :status) AND c.isDeleted = false ")
    Page<Customer> findByKeywordAndStatus(@Param("keyword") String keyword, @Param("status") CustomerStatus status, Pageable pageable);

    /**
     * 이메일로 고객 조회
     * 로그인 시 이메일로 고객을 찾을 때 사용합니다.
     * Optional을 사용하여 존재하지 않는 경우 예외 처리가 가능합니다.
     * @param email 조회할 이메일
     * @return Optional로 감싼 고객 객체
     */
    Optional<Customer> findByEmail(String email);
}

