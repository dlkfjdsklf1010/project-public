package com.commerceapp.product.entity;

import com.commerceapp.admin.entity.Admin;
import com.commerceapp.common.entity.BaseEntity;
import com.commerceapp.product.enums.ProductStatus;
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

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, length = 30)
    private String category;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private int stock;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ProductStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id", nullable = false)
    private Admin admin;

    public Product(String name, String category, int price, int stock, ProductStatus status, Admin admin) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.stock = stock;
        this.status = status;
        this.admin = admin;
    }

    // 생성
    public static Product create(String name, String category, int price, int stock, ProductStatus status, Admin admin) {
        Product product = new Product();
        product.name = name;
        product.category = category;
        product.price = price;
        product.stock = stock;
        product.status = status;
        product.admin = admin;

        return product;
    }

    // 주문 시 수량 변경
    public void decreateStock(int quantity) {
        if (quantity < 1) {
            throw new IllegalArgumentException("주문 수량은 1개 이상이어야 합니다.");
        }

        if (this.status == ProductStatus.DISCONTINUED) {
            throw new IllegalArgumentException("단종 상품은 주문할 수 없습니다.");
        }

        if (this.stock == 0) {
            this.status = ProductStatus.SOLD_OUT;

            throw new IllegalArgumentException("품절 상품은 주문할 수 없습니다.");
        }

        if (this.stock < quantity) {
            throw new IllegalArgumentException("상품의 재고가 부족합니다.");
        }

        this.stock -= quantity;

        if (this.stock == 0) {
            this.status = ProductStatus.SOLD_OUT;
        }
    }

    // 주문 취소 시 수량 변경
    public void increaseStock(int quantity) {
        if (quantity < 1) {
            throw new IllegalArgumentException("취소 수량은 1개 이상이어야 합니다.");
        }

        this.stock += quantity;

        // 품절 상품이 취소되었다면 판매중으로 복구
        if (!this.status.equals("DISCONTINUED") && this.stock > 0) {
            this.status = ProductStatus.ON_SALE;
        }
    }

    // 상품 수정
    public void update(String name, String category, int price) {
        this.name = name;
        this.category = category;
        this.price = price;
    }
}
