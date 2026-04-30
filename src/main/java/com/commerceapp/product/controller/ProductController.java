package com.commerceapp.product.controller;

import com.commerceapp.admin.dto.AdminLoginSession;
import com.commerceapp.common.exception.NotFoundException;
import com.commerceapp.common.exception.UnauthorizedException;
import com.commerceapp.product.dto.*;
import com.commerceapp.product.entity.Product;
import com.commerceapp.product.enums.ProductStatus;
import com.commerceapp.product.repository.ProductRepository;
import com.commerceapp.product.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.parser.Entity;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;
    private final ProductRepository productRepository;

    // 상품 등록
    @PostMapping
    public ResponseEntity<ProductDetailResponse> create(
            @RequestBody ProductCreateRequest request,
            HttpServletRequest httpRequest
    ) {
        AdminLoginSession loginAdmin = getLoginAdmin(httpRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productService.create(request, loginAdmin));
    }

    // 상품 리스트 조회
    @GetMapping
    public ResponseEntity<ProductPageResponse<ProductResponse>> getProductList(
            HttpSession session,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String state,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction
    ) {
        if(session.getAttribute("loginAdmin") == null){
            throw new UnauthorizedException("관리자 로그인이 필요합니다.");
        }

        return ResponseEntity.ok(productService.getProductList(keyword, category, state, page, size, sortBy, direction));
    }

    // 상품 단건 조회
    @GetMapping("/{productId}")
    public ResponseEntity<ProductDetailResponse> getProductDetail(
            @PathVariable Long productId,
            HttpServletRequest request
    ) {
        getLoginAdmin(request);

        return ResponseEntity.ok(productService.getProductDetail(productId));
    }

    // 상품 수정
    @PatchMapping("/{productId}")
    public ResponseEntity<String> updateProduct(
            @PathVariable Long productId,
            @RequestBody ProductUpdateRequest request,
            HttpServletRequest httpRequest
    ) {
        AdminLoginSession loginAdmin = getLoginAdmin(httpRequest);

        productService.updateProduct(
                productId,
                request.getName(),
                request.getCategory(),
                request.getPrice(),
                loginAdmin
        );

        return ResponseEntity.ok("상품 수정이 완료되었습니다.");
    }

    // 상품 상태 변경
    @PatchMapping("/status/{productId}")
    public ResponseEntity<String> updateProductStatus(
            @PathVariable Long productId,
            @RequestBody ProductUpdateStatusRequest request,
            HttpServletRequest httpRequest
    ) {
        AdminLoginSession loginAdmin = getLoginAdmin(httpRequest);

        productService.updateProductStatus(
                productId,
                request.getStatus(),
                loginAdmin
        );

        return ResponseEntity.ok("상품 상태 수정이 완료되었습니다.");
    }

    // 상품 삭제
    @DeleteMapping("/{productId}")
    public ResponseEntity<String> delete(
            @PathVariable Long productId,
            HttpServletRequest request
    ) {
        AdminLoginSession loginAdmin = getLoginAdmin(request);

        productService.deleteProduct(productId, loginAdmin);

        return ResponseEntity.ok("상품이 삭제되었습니다.");
    }

    // 관리자가 로그인 했는지 확인
    private AdminLoginSession getLoginAdmin(HttpServletRequest request) {
        HttpSession session = request.getSession();

        if (session == null) {
            throw new UnauthorizedException("관리자 로그인이 필요합니다.");
        }

        AdminLoginSession admin =
                (AdminLoginSession) session.getAttribute("loginAdmin");

        if (admin == null) {
            throw new UnauthorizedException("관리자 로그인이 필요합니다.");
        }

        return admin;
    }
}
