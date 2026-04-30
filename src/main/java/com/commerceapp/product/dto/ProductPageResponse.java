package com.commerceapp.product.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@JsonPropertyOrder({"page", "size", "productList", "totalProducts", "totalPages"})
@Getter
public class ProductPageResponse<T> {
    private List<T> productList;
    private int page;
    private int size;
    private long totalProducts;
    private int totalPages;

    public ProductPageResponse(List<T> productList, int page, int size, long totalProducts, int totalPages) {
        this.productList = productList;
        this.page = page;
        this.size = size;
        this.totalProducts = totalProducts;
        this.totalPages = totalPages;
    }

    public static <T> ProductPageResponse<T> from(Page<T> page) {
        return new ProductPageResponse<>(
                page.getContent(),
                page.getNumber() + 1,
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }
}