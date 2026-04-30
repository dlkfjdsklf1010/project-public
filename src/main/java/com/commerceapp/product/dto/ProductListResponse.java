package com.commerceapp.product.dto;

import com.commerceapp.product.entity.Product;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
public class ProductListResponse {
    private Long id;
    private String name;
    private int price;
    private int stock;
    private String status;
    private String createdByName;

    public ProductListResponse(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.price = product.getPrice();
        this.stock = product.getStock();
        this.status = product.getStatus().getDisplayName();
        this.createdByName = product.getAdmin().getName();
    }
}
