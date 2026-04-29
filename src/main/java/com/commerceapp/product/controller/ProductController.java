package com.commerceapp.product.controller;

import com.commerceapp.admin.dto.AdminLoginSession;
import com.commerceapp.product.dto.*;
import com.commerceapp.product.enums.ProductStatus;
import com.commerceapp.product.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    // 상품 등록
    @PostMapping
    public ResponseEntity<ProductDetailResponse> create(
            @RequestBody ProductCreateRequest request,
            HttpSession session
    ) {

        return ResponseEntity.status(HttpStatus.CREATED).body(productService.create(request, session));
    }

    // 상품 리스트 조회
    @GetMapping
    public ResponseEntity<ProductPageResponse<ProductResponse>> getProductList(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) ProductStatus state,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction
    ) {
        return ResponseEntity.ok(productService.getProductList(keyword, category, state, page, size, sortBy, direction));
    }

    // 상품 상세 조회
    @GetMapping("/{productId}")
    public ResponseEntity<ProductDetailResponse> getProductDetail(@PathVariable Long productId) {
        return ResponseEntity.ok(productService.getProductDetail(productId));
    }

    /**
     * 상품 수정
     * admin이 로그인했을 때만 수정 가능하도록 코드 수정
     */
    @PatchMapping("/{productId}")
    public void update(@PathVariable Long productId,
                       @RequestBody ProductUpdateRequest request,
                       HttpServletRequest requestHttp
    ) {
        HttpSession session = requestHttp.getSession(false);

        AdminLoginSession loginSession = (AdminLoginSession) session.getAttribute("loginAdmin");
        // 수정할 데이터 꺼내기
        productService.updateProduct(
                productId,
                request.getName(),
                request.getCategory(),
                request.getPrice(),
                loginSession
        );
    }

    /**
     * 상품 삭제
     * admin이 로그인했을 때만 수정 가능하도록 코드 수정
     */
    @DeleteMapping("/{productId}")
    public ResponseEntity<String> delete(@PathVariable Long productId, HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session == null) {
            throw new IllegalStateException("로그인이 필요합니다.");
        }

        AdminLoginSession loginSession = (AdminLoginSession) session.getAttribute("loginAdmin");
        productService.delete(productId, loginSession);

        return ResponseEntity.status(HttpStatus.OK).body("상품이 삭제되었습니다.");
    }
}
