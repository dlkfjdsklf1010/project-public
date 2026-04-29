package com.commerceapp.order.controller;

import com.commerceapp.admin.dto.AdminLoginSession;
import com.commerceapp.order.dto.*;
import com.commerceapp.order.enums.OrderStatus;
import com.commerceapp.order.service.OrderService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/orders")
public class OrderController {
    private final OrderService orderService;

    // 고객 주문 생성
    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(
            @RequestBody OrderCreateRequest request,
            HttpSession session
    ) {
        return ResponseEntity.ok(orderService.createOrder(request, session));
    }

    // CS 주문 생성
    @PostMapping("/admin")
    public ResponseEntity<OrderResponse> createOrderByAdmin(
            @RequestBody OrderCreateByAdminRequest request,
            HttpSession session
    ) {
        return ResponseEntity.ok(orderService.createOrderByAdmin(request, session));
    }

    // 주문 리스트 조회
    @GetMapping
    public ResponseEntity<OrderPageResponse> getOrderList(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction,
            @RequestParam(required = false) OrderStatus status
    ) {
        return ResponseEntity.ok(
                orderService.getOrders(keyword, page, size, sortBy, direction, status)
        );
    }

    // 주문 상세 조회
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDetailResponse> getOrderDetail(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.getOrderDetail(orderId));
    }

    // 주문 상태 변경
    @PatchMapping("/{orderId}/status")
    public ResponseEntity<Void> updateStatus(
            @PathVariable Long orderId,
            @SessionAttribute(name = "loginAdmin", required = false) AdminLoginSession adminSession
    ) {
        if (adminSession == null) {
            throw new IllegalArgumentException("관리자 로그인이 필요합니다.");
        }
        orderService.updateStatus(orderId, adminSession);

        return ResponseEntity.ok().build();
    }

    // 주문 취소
    @PatchMapping("/{orderId}/cancel")
    public ResponseEntity<Void> cancelOrder(
            @PathVariable Long orderId,
            @RequestBody String reason
    ) {
        orderService.cancelOrder(orderId, reason);

        return ResponseEntity.ok().build();
    }
}
