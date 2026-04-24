package com.commerceapp.customers.entity;

import com.commerceapp.common.entity.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "customers")
public class Customers extends BaseEntity {

    /*========== 속성 ===========*/

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; /* 고객 고유 식별자 */

    private String name; /* 고객 이름 */

    @Column(unique = true)
    private String email; /* 이메일 (로그인 아이디) */

    private String password; /* 비밀번호 (암호화 저장) */

    private String phoneNumber; /* 전화번호 (010-XXXX-XXXX) */

    private  String status; /* 상태 (활성/비활성/정지) */

    /*========== 생성자 ===========*/

    protected  Customers() {
    }
    public Customers(String name, String email, String password, String phoneNumber) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.status = "활성"; /* 기본값 */
    }

    /*========== 기능 ===========*/

    /* 고객 정보 수정 */
    public void updateCustomers(String name, String email, String phoneNumber) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    /* 고객 상태 변경 */
    public void updateStatus(String status) {
        this.status = status;
    }
}
