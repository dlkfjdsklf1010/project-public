package com.commerceapp.order.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class OrderCreateRequest {
    private List<OrderItemRequest> items;
}
