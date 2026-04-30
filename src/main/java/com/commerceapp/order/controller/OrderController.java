package com.commerceapp.order.controller;

import com.commerceapp.admin.dto.AdminLoginSession;
import com.commerceapp.common.exception.UnauthorizedException;
import com.commerceapp.order.dto.*;
import com.commerceapp.order.enums.OrderStatus;
import com.commerceapp.order.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
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
    public ResponseEntity<OrderResponse> createOrderbyUser(
            @Valid @RequestBody OrderCreateRequest request,
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
            @RequestParam(required = false) OrderStatus status,
            HttpServletRequest request
    ) {
       HttpSession session = request.getSession(false);

       if (session == null) {
           throw new UnauthorizedException("관리자 로그인이 필요합니다.");
       }

       AdminLoginSession loginSession = (AdminLoginSession) session.getAttribute("loginAdmin");

       if (loginSession == null) {
           throw new UnauthorizedException("관리자 로그인이 필요합니다.");
       }

       OrderPageResponse response = orderService.getOrderList(
               keyword, page, size, sortBy, direction, status);

        return ResponseEntity.ok(response);
    }

    // 주문 상세 조회
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDetailResponse> getOrderDetail(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.getOrderDetail(orderId));
    }

    // 주문 상태 변경
    @PatchMapping("/{orderId}/status")
    public ResponseEntity<String> updateStatus(
            @PathVariable Long orderId,
            @SessionAttribute(name = "loginAdmin", required = false) AdminLoginSession adminSession
    ) {
        if (adminSession == null) {
            throw new UnauthorizedException("관리자 로그인이 필요합니다.");
        }
        orderService.updateStatus(orderId, adminSession);

        return ResponseEntity.ok().body("주문 상태 변경이 완료되었습니다.");
    }

    // 주문 취소
    @PatchMapping("/{orderId}/cancel")
    public ResponseEntity<String> cancelOrder(
            @PathVariable Long orderId,
            @RequestBody String reason
    ) {
        orderService.cancelOrder(orderId, reason);

        return ResponseEntity.ok().body("주문이 취소되었습니다.");
    }
}
