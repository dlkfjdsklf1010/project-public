package com.commerceapp.product.controller;

import com.commerceapp.product.dto.ProductCreateRequest;
import com.commerceapp.product.dto.ProductDetailResponse;
import com.commerceapp.product.dto.ProductResponse;
import com.commerceapp.product.dto.ProductUpdateRequest;
import com.commerceapp.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/products")
public class ProductController {

    //속성
    private final ProductService productService;

    // 상품 등록
    @PostMapping
    public ProductDetailResponse create(@RequestBody ProductCreateRequest request) {
        String adminName = "관리자";
        String adminEmail = "admintest@product.com";

        return productService.create(request, adminName, adminEmail);
    }

    // 상품 전체 조회
    @GetMapping
    public List<ProductResponse> getAllproducts() {
        return productService.getAllproducts();
    }


    // 상품 상세 조회
    @GetMapping("/{productId}")
    public ProductDetailResponse getDetail(@PathVariable Long productId) {
        return productService.getDetail(productId);
    }

    // 상품 수정
    @PatchMapping("/{productId}")
    public void update(@PathVariable Long productId,
                       @RequestBody ProductUpdateRequest request) {
        // 수정할 데이터 꺼내기
        productService.update(
                productId,
                request.getName(),
                request.getCategory(),
                request.getPrice(),
                request.getStock()
        );
    }

    // 상품 삭제
    @DeleteMapping("/{productId}")
    public void delete(@PathVariable Long productId) {
        productService.delete(productId);
    }
}
