package com.commerceapp.order.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class OrderCreateByAdminRequest {
    private Long customerId;
    private List<OrderItemRequest> items;
}
