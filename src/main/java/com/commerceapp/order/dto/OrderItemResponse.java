package com.commerceapp.order.dto;

import com.commerceapp.order.entity.OrderItem;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;

@Getter
@JsonPropertyOrder({"productName", "price", "quantity", "totalSum"})
public class OrderItemResponse {
    private String productName;
    private int price;
    private int quantity;
    private int totalSum;

    public OrderItemResponse(OrderItem item) {
        this.productName = item.getProductName();
        this.price = item.getPrice();
        this.quantity = item.getQuantity();
        this.totalSum = item.getTotalSum();
    }
}
