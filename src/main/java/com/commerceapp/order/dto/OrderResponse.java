package com.commerceapp.order.dto;

import com.commerceapp.order.entity.Order;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class OrderResponse {
    private String orderNumber;
    private String customerName;
    private String adminName;
    private int totalPrice;
    private String status;
    private LocalDateTime createdAt;
    private List<OrderItemResponse> items;

    public OrderResponse(Order order) {
        this.orderNumber = order.getOrderNumber();
        this.customerName = order.getCustomer().getName();

        // 관리자는 null 가능
        this.adminName = order.getAdmin() != null
                ? order.getAdmin().getName()
                : null;

        this.totalPrice = order.getTotalPrice();
        this.status = order.getStatus().name();
        this.createdAt = order.getCreatedAt();

        // OrderItem -> DTO 변환
        this.items = order.getOrderItems().stream()
                .map(OrderItemResponse::new)
                .collect(Collectors.toList());
    }
}
