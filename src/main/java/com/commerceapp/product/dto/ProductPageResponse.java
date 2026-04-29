package com.commerceapp.product.dto;

import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
public class ProductPageResponse<T> {

    private List<T> content;
    private int page;
    private int size;
    private long totalProducts;
    private int totalPages;

    public ProductPageResponse(List<T> content, int page, int size, long totalProducts, int totalPages) {
        this.content = content;
        this.page = page;
        this.size = size;
        this.totalProducts = totalProducts;
        this.totalPages = totalPages;
    }

    /**
     * Page 기능을 사용할 수 있도록 수정
     */
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
