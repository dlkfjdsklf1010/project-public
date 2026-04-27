package com.commerceapp.order.dto;

import com.commerceapp.order.entity.Order;
import com.commerceapp.order.entity.OrderStatus;
import lombok.Getter;

@Getter
public class OrderSimpleResponse {
    private Long id;
    private String orderNumber;
    private String customerName;
    private int totalPrice;
    private OrderStatus status;

    public OrderSimpleResponse(Order order) {
        this.id = order.getId();
        this.orderNumber = order.getOrderNumber();
        this.customerName = order.getCustomer().getName();
        this.totalPrice = order.getTotalPrice();
        this.status = order.getStatus();
    }
}
