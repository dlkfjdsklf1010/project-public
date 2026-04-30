package com.commerceapp.order.dto;

import com.commerceapp.order.entity.OrderItem;
import lombok.Getter;

@Getter
public class OrderItemDto {
    private String productName;
    private int quantity;
    private int totalPrice;

    public OrderItemDto(OrderItem item) {
        this.productName = item.getProduct().getName();
        this.quantity = item.getQuantity();
        this.totalPrice = item.getTotalSum();
    }
}
