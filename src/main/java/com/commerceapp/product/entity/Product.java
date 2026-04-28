package com.commerceapp.product.entity;

import com.commerceapp.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "products")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(nullable = false, length = 30)
    private String category;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private int stock;

    @Column(nullable = false, length = 30)
    private ProductStatus state = ProductStatus.ON_SALE;

//    // 등록 관리자 이름
//    @Column(nullable = false, length = 20)
//    private String createdByName;
//
//    // 등록 관리자 이메일
//    @Column(nullable = false, length = 255)
//    private String createdByEmail;

    public Product(String name, String category, int price, int stock) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.stock = stock;
//        this.createdByName = createdByName;
//        this.createdByEmail = createdByEmail;
    }

    public static Product create(
            String name,
            String category,
            int price,
            int stock,
            ProductStatus state
    ) {
        Product product = new Product();
        product.name = name;
        product.category = category;
        product.price = price;
        product.stock = stock;
        product.state = state;
        return product;
    }

    // 상품 수정
    public void update(String name, String category, int price, int stock) {
        this.name = name;
        this.category = category;
        this.price = price;
    }

    public void decreateStock(int quantity) {
        if (quantity < 1) {
            throw new IllegalArgumentException("수량은 1개 이상이어야 합니다.");
        }

        if (this.state == ProductStatus.DISCONTINUED) {
            throw new IllegalArgumentException("단종 상품은 주문할 수 없습니다.");
        }

        if (this.state == ProductStatus.SOLD_OUT) {
            throw new IllegalArgumentException("품절 상품은 주문할 수 없습니다.");
        }

        if (this.stock < quantity) {
            throw new IllegalArgumentException("상품의 재고가 부족합니다.");
        }

        this.stock -= quantity;

        // 재고 0 되면 자동 품절
        if (this.stock == 0) {
            this.state = ProductStatus.SOLD_OUT;
        }
    }

    public void increaseStock(int quantity) {
        if (quantity < 1) {
            throw new IllegalArgumentException("수량은 1개 이상이어야 합니다.");
        }

        this.stock += quantity;

        // 단종이 아니면 판매중으로 복구
        if (this.state != ProductStatus.DISCONTINUED && this.stock > 0) {
            this.state = ProductStatus.ON_SALE;
        }
    }

    public enum ProductStatus {
        ON_SALE,
        SOLD_OUT,
        DISCONTINUED
    }
}
