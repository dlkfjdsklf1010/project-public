package com.commerceapp.product.dto;

import lombok.Getter;

@Getter
public class ProductUpdateRequest {
    private String name;
    private String category;
    private int price;
}
