package com.commerceapp.product.controller;

import com.commerceapp.product.dto.ProductCreateRequest;
import com.commerceapp.product.dto.ProductDetailResponse;
import com.commerceapp.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/products")
public class ProductController {

    //속성
    private final ProductService productService;

    // 상품 등록
    @PostMapping
    public ProductDetailResponse create(@RequestBody ProductCreateRequest request) {
        Long adminId = 1L;
        String adminName = "관리자";
        String adminEmail = "admintest@product.com";
        return productService.create(request, adminId, adminName, adminEmail);
    }


    // 상품 상세 조회
    @GetMapping("/{productId}")
    public ProductDetailResponse getDetail(@PathVariable Long productId) {
        return productService.getDetail(productId);
    }
}
