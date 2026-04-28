package com.commerceapp.product.service;
import com.commerceapp.product.dto.ProductResponse;
import com.commerceapp.product.entity.Product;
import com.commerceapp.product.dto.ProductCreateRequest;
import com.commerceapp.product.dto.ProductDetailResponse;
import com.commerceapp.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    // 상품 등록
    @Transactional
    // 컨트롤러 -> 데이터 받기
    public ProductDetailResponse create(ProductCreateRequest request, String adminName, String adminEmail) {
        // 엔티티 생성
        Product product = new Product(
                request.getName(),
                request.getCategory(),
                request.getPrice(),
                request.getStock()
//                adminName,
//                adminEmail
        );

        // db에 저장
        Product saved = productRepository.save(product);

        // 엔티티 -> dto로 변환 후 반환
        return new ProductDetailResponse(saved);

    }

    // 상품 전체 조회
    @Transactional(readOnly = true)
    public List<ProductResponse> getAllproducts() {
        // db에서 전체 조회
        List<Product> products = productRepository.findAll();

        // 엔티티 -> dto 변환
        return products.stream()
                .map(ProductResponse::new)
                .collect(Collectors.toList());
    }

    // 상품 상세 조회
    @Transactional(readOnly = true)
    public ProductDetailResponse getDetail(Long productId) {

        // 아이디로 상품 조회
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품이 없습니다"));

        // 엔티티 -> dto
        return new ProductDetailResponse(product);
    }

    // 상품 수정
    @Transactional
    public void update(Long productId, String name, String category, int price, int stock) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품이 존재하지 않습니다."));

        product.update(name, category, price, stock);
    }

    // 상품 삭제
    @Transactional
    public void delete(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품이 존재하지 않습니다."));

        //db에서 해당 상품 삭제
        productRepository.delete(product);
    }
}
