package com.commerceapp.product.controller;

import com.commerceapp.product.dto.ProductResponse;
import com.commerceapp.product.dto.ProductUpdateRequest;
import com.commerceapp.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // 전체 조회
    @GetMapping
    public List<ProductResponse> getAllProducts() {

        // Service 호출
        return productService.getAllProducts();
    }

    // 상품 수정
    @PatchMapping("/{productId}")
    public void update(@PathVariable Long productId,
                       @RequestBody ProductUpdateRequest request) {

        // 1. URL에서 productId 추출
        // 2. 요청 body에서 수정할 데이터 받아오기

        productService.update(
                productId,
                request.getName(),            // 요청 데이터에서 name 꺼내기
                request.getCategory(),       // category 꺼내기
                request.getPrice(),         // price 꺼내기
                request.getStock()         //  stock 꺼내기
        );

        // 3. Service로 로직 위임
    }

    // 상품 삭제
    @DeleteMapping("/{productId}")
    public void delete(@PathVariable Long productId) {

        // 1. URL에서 productId 추출
        productService.delete(productId);

        // 2. Service에서 삭제 처리
    }
}
