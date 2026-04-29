package com.commerceapp.product.dto;

import com.commerceapp.product.entity.Product;
import com.commerceapp.product.enums.ProductStatus;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ProductDetailResponse {
    private String name;
    private String category;
    private int price;
    private int stock;
    private ProductStatus status;
    private String createdByName;
    private String createdByEmail;
    private LocalDateTime createdAt;

    public ProductDetailResponse(Product product) {
        this.name = product.getName();
        this.category = product.getCategory();
        this.price = product.getPrice();
        this.stock = product.getStock();
        this.status = product.getStatus();
        this.createdByName = product.getAdmin().getName();
        this.createdByEmail = product.getAdmin().getEmail();
        this.createdAt = product.getCreatedAt();
    }
}
