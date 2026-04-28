package com.commerceapp.product.entity;

import com.commerceapp.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "products")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseEntity {

    //속성

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //  상품 이름
    @Column(nullable = false, length = 255)
    private String name;

    // 카테고리
    @Column(nullable = false, length = 30)
    private String category;

    // 가격
    @Column(nullable = false)
    private int price;

    // 재고
    @Column(nullable = false)
    private int stock;

    // 상태
    @Column(nullable = false, length = 30)
    private String state;

    // 등록일
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // 등록 관리자 이름
    @Column(nullable = false, length = 20)
    private String createdByName;

    // 등록 관리자 이메일
    @Column(nullable = false, length = 255)
    private String createdByEmail;



    //생성자
    public Product(String name, String category, int price, int stock,
                   String state, String createdByName, String createdByEmail) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.stock = stock;
        this.state = state;
        this.createdByName = createdByName;
        this.createdByEmail = createdByEmail;
    }


    //기능

    // 상품 등록 후 자동으로 처리
    @PrePersist
    public void autoCreate() {
        this.createdAt = LocalDateTime.now();

        // 기본값 "판매중"
        if (this.state == null) {
            this.state = "ON SALE";
        }
    }

    // 상품 수정
    public void update(String name, String category, int price, int stock) {
        this.name = name;
        this.category = category;
        this.price = price;
    }

    // 재고 변경
    public void updateStock(int quantity) {
        this.stock += quantity;
        // 단종이 아닐경우 변경
        if (!this.state.equals("DISCONTINUED")) {
            if (this.stock <= 0) {
                this.state = "SOLD_OUT";
            } else {this.state = "ON_SALE";
            }
        }
    }

    // 상태 변경
    public void changeState(String state) {
        this.state = state;
    }

}
