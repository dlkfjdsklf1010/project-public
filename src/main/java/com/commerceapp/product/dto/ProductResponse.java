package com.commerceapp.product.dto;

import com.commerceapp.product.entity.Product;
import lombok.Getter;

@Getter
public class ProductResponse {

    private String name;
    private String category;
    private int price;
    private int stock;

    public ProductResponse(Product product) {
        this.name = product.getName();
        this.category = product.getCategory();
        this.price = product.getPrice();
        this.stock = product.getStock();
    }

}
