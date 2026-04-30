package com.commerceapp.product.dto;

import com.commerceapp.product.enums.ProductStatus;
import lombok.Getter;

@Getter
public class ProductUpdateStatusRequest {
    private ProductStatus status;
}
