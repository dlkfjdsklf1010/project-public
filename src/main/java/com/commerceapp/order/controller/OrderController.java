package com.commerceapp.order.controller;

import com.commerceapp.order.dto.OrderCreateRequest;
import com.commerceapp.order.dto.OrderDetailResponse;
import com.commerceapp.order.dto.OrderPageResponse;
import com.commerceapp.order.dto.OrderResponse;
import com.commerceapp.order.entity.OrderStatus;
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

    // 주문 생성
    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(
            @RequestBody OrderCreateRequest request,
            HttpSession session
    ) {
        return ResponseEntity.ok(orderService.createOrder(request, session));
    }

    // 주문 리스트 조회
    @GetMapping
    public ResponseEntity<OrderPageResponse> getOrders(
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

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDetailResponse> getOrderDetail(
            @PathVariable Long orderId
    ) {
        return ResponseEntity.ok(orderService.getOrderDetail(orderId));
    }

    // 주문 상태 변경
    @PatchMapping("/{orderId}/status")
    public ResponseEntity<Void> updateStatus(@PathVariable Long orderId) {
        orderService.updateStatus(orderId);

        return ResponseEntity.ok().build();
    }

    // 주문 취소
    @PatchMapping("/{orderId}/cancel")
    public ResponseEntity<Void> cancel(
            @PathVariable Long orderId,
            @RequestBody String reason
    ) {
        orderService.cancelOrder(orderId, reason);

        return ResponseEntity.ok().build();
    }
}
