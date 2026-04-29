package com.commerceapp.order.dto;

import com.commerceapp.order.entity.Order;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@JsonPropertyOrder({"id", "orderNumber", "customerName", "itemList", "totalPrice", "createdAt", "status", "adminName"})
public class OrderResponse {
    private Long id;
    private String orderNumber;
    private String customerName;
    private int totalPrice;
    private LocalDateTime createdAt;
    private String status;
    private String adminName;
    private List<OrderItemResponse> itemList;

    public OrderResponse(Order order) {
        this.id = order.getId();
        this.orderNumber = order.getOrderNumber();
        this.customerName = order.getCustomer().getName();

        // OrderItem -> DTO 변환
        this.itemList = order.getOrderItemList().stream()
                .map(OrderItemResponse::new)
                .collect(Collectors.toList());

        this.totalPrice = order.getTotalPrice();
        this.status = order.getStatus().name();
        this.createdAt = order.getCreatedAt();

        // 관리자는 null 가능
        this.adminName = order.getAdmin() != null
                ? order.getAdmin().getName()
                : null;
    }
}
