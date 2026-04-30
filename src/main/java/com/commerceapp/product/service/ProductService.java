package com.commerceapp.product.service;

import com.commerceapp.admin.dto.AdminLoginSession;
import com.commerceapp.admin.entity.Admin;
import com.commerceapp.admin.repository.AdminRepository;
import com.commerceapp.common.exception.NotFoundException;
import com.commerceapp.common.exception.UnauthorizedException;
import com.commerceapp.product.dto.*;
import com.commerceapp.product.entity.Product;
import com.commerceapp.product.enums.ProductStatus;
import com.commerceapp.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final AdminRepository adminRepository;

    // 등록
    @Transactional
    public ProductDetailResponse create(ProductCreateRequest request, AdminLoginSession loginAdmin) {
        Admin admin = adminRepository.findById(loginAdmin.getId())
                .orElseThrow(() -> new NotFoundException("해당 관리자가 존재하지 않습니다."));

        Product product = new Product(
                request.getName(),
                request.getCategory(),
                request.getPrice(),
                request.getStock(),
                request.getStatus(),
                admin
        );

        Product savedProduct = productRepository.save(product);

        return new ProductDetailResponse(savedProduct);
    }

    // 상품 리스트 조회
    @Transactional(readOnly = true)
    public ProductPageResponse<ProductResponse> getProductList(
            String keyword,
            String category,
            String status,
            int page,
            int size,
            String sortBy,
            String direction
    ) {
        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page - 1, size, sort);

        Page<Product> result = productRepository.search(
                keyword,
                category,
                status,
                pageable
        );

        Page<ProductResponse> responsePage =
                result.map(ProductResponse::new);

        return ProductPageResponse.from(responsePage);
    }

    // 상품 상세 조회
    @Transactional(readOnly = true)
    public ProductDetailResponse getProductDetail(Long productId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("해당 상품이 존재하지 않습니다."));

        if (product.getIsDeleted()) {
            throw new NotFoundException("삭제된 상품입니다.");
        }

        // 엔티티 -> dto
        return new ProductDetailResponse(product);
    }

    /**
     * 상품 수정
     * admin이 로그인했을 때만 수정 가능하도록 코드 수정
     */
    @Transactional
    public void updateProduct(Long productId, String name, String category, int price, AdminLoginSession adminSession) {
        if (adminSession == null) {
            throw new UnauthorizedException("관리자만 상품 수정이 가능합니다.");
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("해당 상품이 존재하지 않습니다."));

        product.update(name, category, price);
    }

    // 상품 상태 변경
    @Transactional
    public void updateProductStatus(Long productId, ProductStatus status, AdminLoginSession adminSession) {
        if (adminSession == null) {
            throw new UnauthorizedException("관리자만 상품 수정이 가능합니다.");
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("상품이 존재하지 않습니다."));

        product.updateStatus(status);
    }

    // 상품 삭제 (소프트 딜리트)
    @Transactional
    public Product deleteProduct(Long productId, AdminLoginSession adminSession) {
        if (adminSession == null) {
            throw new UnauthorizedException("관리자만 상품 수정이 가능합니다.");
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("상품이 존재하지 않습니다."));
        product.delete();

        return product;
    }
}
