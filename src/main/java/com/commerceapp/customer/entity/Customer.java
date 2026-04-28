package com.commerceapp.customer.entity;

import com.commerceapp.common.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;


/**
 * 고객 엔티티 클래스
 * DB의 customers 테이블과 매핑되며, 고객 정보를 관리합니다.
 * BaseEntity를 상속받아 createdAt을 자동으로 관리합니다.
 */
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Entity
@Table(name = "customers")
public class Customer extends BaseEntity {

    /*========== 속성 ===========*/

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) /* PK 자동 증가 (AUTO_INCREMENT) */
    private Long id; /* 고객 고유 식별자 */

    @Column(nullable = false, length = 20) /* NOT NULL, 최대 20자 */
    private String name; /* 고객 이름 */

    @Column(nullable = false, unique = true) /* NOT NULL, 중복 불가 */
    private String email; /* 이메일 (로그인 아이디) */

    @Column(nullable = false) /* NOT NULL */
    private String password; /* 비밀번호 (암호화 저장) */

    @Column(nullable = false, length = 20) /* NOT NULL, 최대 20자 */
    @Pattern(regexp = "^010-\\d{4}-\\d{4}$") /* 010-XXXX-XXXX 형식 유효성 검증 */
    private String phoneNumber; /* 전화번호 */

    @Column(nullable = false, length = 20) /* NOT NULL, 최대 20자 */
    private  String status; /* 상태 (ACTIVE/INACTIVE/SUSPENDED) */

    @Column(nullable = false) /* NOT NULL */
    private Boolean isDeleted = false; /* 소프트 딜리트 여부 (기본값: false) */

    /*========== 생성자 ===========*/

    /**
     * 고객 생성 생성자
     * 신규 고객 생성 시 사용하며, 기본 상태는 ACTIVE로 설정됩니다.
     */
    public Customer(String name, String email, String password, String phoneNumber) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.status = "ACTIVE"; /* 신규 고객 기본 상태 */
    }

    /*========== 기능 ===========*/

    /**
     * 고객 정보 수정
     * @Transactional 환경에서 더티체킹으로 자동 UPDATE 쿼리 실행
     */
    public void updateCustomers(String name, String email, String phoneNumber) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    /**
     * 고객 상태 변경
     * 상태값: ACTIVE(활성), INACTIVE(비활성), SUSPENDED(정지)
     */
    public void updateStatus(String status) {
        this.status = status;
    }

    /**
     * 고객 소프트 딜리트
     * DB에서 실제로 삭제하지 않고 isDeleted 플래그를 true로 변경
     */
    public void delete() {
        this.isDeleted = true;
    }

    /**
     * 더미 데이터 생성용 정적 팩토리 메서드
     * DataInitializer에서 테스트 데이터 생성 시 사용
     */
    public static Customer create(String name, String email, String password, String phoneNumber, String status) {
        Customer customer = new Customer();
        customer.name = name;
        customer.email = email;
        customer.password = password;
        customer.phoneNumber = phoneNumber;
        customer.status = status;

        return customer;
    }

}
