package com.commerceapp.product.controller;


import com.commerceapp.product.dto.ProductUpdaterequest;
import com.commerceapp.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // 상품 수정
    @PostMapping("/{productId")
    public void update(@PathVariable Long prductId,
                       @RequestBody ProductUpdaterequest request) {

        // 1. URL에서 productId 추출
        // 2. 요청 body에서 수정할 데이터 받아오기

        productService.update(
                prductId,
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
