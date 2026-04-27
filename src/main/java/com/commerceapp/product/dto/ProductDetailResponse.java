package com.commerceapp.product.dto;

import com.commerceapp.product.entity.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Getter
public class ProductDetailResponse {
    private String name;
    private String category;
    private int price;
    private int stock;
    private String state;
    private String createdByName;
    private String createdByEmail;

}
