package com.commerceapp.order.dto;

import com.commerceapp.order.entity.Order;
import com.commerceapp.order.entity.OrderItem;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@JsonPropertyOrder({
        "orderId",
        "orderNumber",
        "customerName",
        "itemList",
        "adminName",
        "createdAt",
        "status"
})
@Getter
public class OrderGroupedResponse {
    private Long orderId;
    private String orderNumber;
    private String customerName;
    private String status;
    private LocalDateTime createdAt;
    private String adminName;
    private List<OrderItemDto> itemList = new ArrayList<>();

    public OrderGroupedResponse(Order order) {
        this.orderId = order.getId();
        this.orderNumber = order.getOrderNumber();
        this.customerName = order.getCustomer().getName();
        this.status = order.getStatus().getDisplayName();
        this.createdAt = order.getCreatedAt();
        this.adminName = order.getAdmin() != null
                ? order.getAdmin().getName()
                : null;
    }

    public void addItem(OrderItem item) {
        this.itemList.add(new OrderItemDto(item));
    }
}
