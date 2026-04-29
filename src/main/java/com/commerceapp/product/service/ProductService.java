package com.commerceapp.product.service;

import com.commerceapp.admin.dto.AdminLoginSession;
import com.commerceapp.admin.entity.Admin;
import com.commerceapp.admin.repository.AdminRepository;
import com.commerceapp.product.dto.ProductPageResponse;
import com.commerceapp.product.dto.ProductResponse;
import com.commerceapp.product.entity.Product;
import com.commerceapp.product.dto.ProductCreateRequest;
import com.commerceapp.product.dto.ProductDetailResponse;
import com.commerceapp.product.enums.ProductStatus;
import com.commerceapp.product.repository.ProductRepository;
import jakarta.servlet.http.HttpSession;
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
    public ProductDetailResponse create(ProductCreateRequest request, HttpSession session) {
        AdminLoginSession loginAdmin = (AdminLoginSession) session.getAttribute("loginAdmin");

        if (loginAdmin == null) {
            throw new IllegalArgumentException("관리자 로그인 후 이용 가능합니다.");
        }

        Admin admin = adminRepository.findById(loginAdmin.getId())
                .orElseThrow(() -> new IllegalArgumentException("관리자 정보를 찾을 수 없습니다."));

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
            ProductStatus state,
            int page,
            int size,
            String sortBy,
            String direction
    ) { //정렬 조건 만들기
        Sort sort = direction.equalsIgnoreCase("desc")  // 정렬 조건이 "desc"
                ? Sort.by(sortBy).descending()  // 내림차순
                : Sort.by(sortBy).ascending();   // 오름차순
        //spring에서는 페이지가 0부터 시작해야 함
        Pageable pageable = PageRequest.of(page - 1, size, sort);

        // db에서 전체 조회
        Page<Product> result = productRepository.findAll(pageable);

        // 엔티티 -> dto 변환
        // mapped -> page 데이터를 변환
        Page<ProductResponse> mapped = result.map(ProductResponse::new);

        // pageResponse로 변환 -> 반환
        return ProductPageResponse.from(mapped);
    }

    // 상품 상세 조회
    @Transactional(readOnly = true)
    public ProductDetailResponse getProductDetail(Long productId) {

        // 아이디로 상품 조회
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품이 없습니다"));

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
            throw new IllegalArgumentException("관리자만 상품 수정이 가능합니다.");
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품이 존재하지 않습니다."));

        product.update(name, category, price);
    }

    /**
     * 상품 삭제
     * admin이 로그인했을 때만 수정 가능하도록 코드 수정
     */
    @Transactional
    public void delete(Long productId, AdminLoginSession adminSession) {
        if (adminSession == null) {
            throw new IllegalArgumentException("관리자만 상품 수정이 가능합니다.");
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품이 존재하지 않습니다."));

        //db에서 해당 상품 삭제
        productRepository.delete(product);
    }
}
