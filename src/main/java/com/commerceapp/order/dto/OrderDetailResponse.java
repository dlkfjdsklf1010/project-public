package com.commerceapp.order.dto;

import com.commerceapp.order.entity.Order;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@JsonPropertyOrder({
        "orderNumber",
        "customerName",
        "customerEmail",
        "itemList",
        "totalPrice",
        "createdAt",
        "status",
        "adminName",
        "adminEmail",
        "adminRole"
})
@Getter
public class OrderDetailResponse {

    private String orderNumber;

    private String customerName;
    private String customerEmail;

    private String adminName;
    private String adminEmail;
    private String adminRole;

    private int totalPrice;
    private String status;
    private LocalDateTime createdAt;

    private List<OrderItemResponse> itemList;

    public OrderDetailResponse(Order order) {
        this.orderNumber = order.getOrderNumber();

        this.customerName = order.getCustomer().getName();
        this.customerEmail = order.getCustomer().getEmail();

        if (order.getAdmin() != null) {
            this.adminName = order.getAdmin().getName();
            this.adminEmail = order.getAdmin().getEmail();
            this.adminRole = order.getAdmin().getRole().getDisplayName();
        }

        this.totalPrice = order.getTotalPrice();
        this.status = order.getStatus().getDisplayName();
        this.createdAt = order.getCreatedAt();

        this.itemList = order.getOrderItemList().stream()
                .map(OrderItemResponse::new)
                .toList();
    }
}
