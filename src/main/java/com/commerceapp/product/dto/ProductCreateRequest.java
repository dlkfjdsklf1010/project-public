package com.commerceapp.product.dto;

import com.commerceapp.product.enums.ProductStatus;
import lombok.Getter;

@Getter
public class ProductCreateRequest {
    private String name;
    private String category;
    private int price;
    private int stock;
    private ProductStatus status;
}
