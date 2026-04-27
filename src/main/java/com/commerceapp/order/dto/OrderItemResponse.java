package com.commerceapp.order.dto;

import com.commerceapp.order.entity.OrderItem;
import lombok.Getter;

@Getter
public class OrderItemResponse {
    private String productName;
    private int price;
    private int quantity;
    private int totalPrice;

    public OrderItemResponse(OrderItem item) {
        this.productName = item.getProductName();
        this.price = item.getPrice();
        this.quantity = item.getQuantity();
        this.totalPrice = item.getTotalPrice();
    }
}
