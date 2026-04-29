package com.commerceapp.product.dto;

import com.commerceapp.product.entity.Product;
import com.commerceapp.product.enums.ProductStatus;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ProductResponse {
    private final Long id;
    private final String name;
    private final String category;
    private final int price;
    private final int stock;
    private final ProductStatus status;
    private final LocalDateTime createdAt;
    private final String createdByName;

    public ProductResponse(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.category = product.getCategory();
        this.price = product.getPrice();
        this.stock = product.getStock();
        this.status = product.getStatus();
        this.createdAt = product.getCreatedAt();
        this.createdByName = product.getAdmin().getName();
    }
}
