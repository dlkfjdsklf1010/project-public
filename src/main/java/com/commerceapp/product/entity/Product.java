package com.commerceapp.product.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;


@Entity
@Table(name = "Products")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product {

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
    public void update(String name, String category, int price) {
        this.name = name;
        this.category = category;
        this.price = price;
    }

    // 재고 변경
    public void updateStock(int quantity) {
        this.stock += quantity;

        if (!this.state.equals("DISCONTINUED")) {
            if (this.stock <= 0) {
                this.stock = Integer.parseInt("SOLD_OUT");  // string 타입을 int 로 변환
            } else {this.state = "ON_SALE";
            }
        }
    }

    // 상태 변경
    public void changeState(String state) {
        this.state = state;
    }

}
