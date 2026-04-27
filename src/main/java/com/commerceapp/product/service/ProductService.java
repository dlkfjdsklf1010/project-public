package com.commerceapp.product.service;


import com.commerceapp.product.dto.ProductResponse;
import com.commerceapp.product.entity.Product;
import com.commerceapp.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    // 전체 조회
    public List<ProductResponse> getAllProducts() {

        // 1. DB에서 전체 조회
        List<Product> products = productRepository.findAll();

        // 2. Entity -> DTO 변환
        return products.stream()
                .map(ProductResponse::new)
                .collect(Collectors.toList());
    }
    // 상품 수정
    public void update(Long productId, String name, String category, int price, int stock) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품이 존재하지 않습니다."));

        product.update(name, category, price, stock);
    }

    // 상품 삭제
    public void delete(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품이 존재하지 않습니다."));

        productRepository.delete(product);
    }
}
