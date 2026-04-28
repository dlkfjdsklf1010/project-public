package com.commerceapp.product.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ProductCreateRequest {

    private String name;
    private String category;
    private int price;
    private int stock;
    private String state;
}
