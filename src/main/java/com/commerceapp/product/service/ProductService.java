package com.commerceapp.product.service;
import com.commerceapp.product.entity.Product;
import com.commerceapp.product.dto.ProductCreateRequest;
import com.commerceapp.product.dto.ProductDetailResponse;
import com.commerceapp.product.entity.ProductStatus;
import com.commerceapp.product.repository.ProductRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Builder  //빌드 패턴으로 객체 생성
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    // 상품 등록
    @Transactional
    // 컨트롤러 -> 데이터 받기
    public ProductDetailResponse create(ProductCreateRequest request, Long adminID, String adminName,
                                        String adminEmail) {

        Product product = Product.builder()
                .name(request.getName())
                .category(request.getCategory())
                .price(request.getPrice())
                .stock(request.getStock())
                .state(ProductStatus.valueOf(request.getState()))

                .createdBy(adminID)
                .createdByName(adminName)
                .createdByEmail(adminEmail)
                .build();
        //
        Product saved = productRepository.save(product);
        return ProductDetailResponse.from(saved);
    }

    // 상품 상세 조회
    @Transactional(readOnly = true)
    public ProductDetailResponse getDetail(Long productId) {

        // 아이디로 상품 조회
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품이 없습니다"));

        // 엔티티 -> dto
        return ProductDetailResponse.from(product);
    }
}
