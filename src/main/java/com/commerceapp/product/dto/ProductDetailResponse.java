package com.commerceapp.product.dto;

import com.commerceapp.product.entity.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@NoArgsConstructor
@Getter
public class ProductDetailResponse {

    // 속성
    private String name;
    private String category;
    private int price;
    private int stock;
    private String state;
    private String createdByName;
    private String createdByEmail;
    private LocalDateTime createdAt;

    // 생성자
    public ProductDetailResponse(Product product) {
        this.name = product.getName();
        this.category = product.getCategory();
        this.price = product.getPrice();
        this.stock = product.getStock();
        this.state = product.getState().name();
//        this.createdByName = product.getCreatedByName();
//        this.createdByEmail = product.getCreatedByEmail();
        this.createdAt = product.getCreatedAt();
    }
}
