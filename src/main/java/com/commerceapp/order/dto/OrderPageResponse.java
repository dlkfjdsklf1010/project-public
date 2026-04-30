package com.commerceapp.order.dto;

import com.commerceapp.order.entity.Order;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@JsonPropertyOrder({
        "page",
        "size",
        "orderList",
        "totalElements",
        "totalPages"
})
@Getter
public class OrderPageResponse {
    private int page;
    private int size;
    private Long totalElements;
    private int totalPages;
    private List<OrderGroupedResponse> orderList;

    public static OrderPageResponse from(Page<Order> pageResult) {
        Map<Long, OrderGroupedResponse> map = new LinkedHashMap<>();

        List<OrderGroupedResponse> orderList = pageResult.getContent().stream()
                .map(order -> {
                    OrderGroupedResponse dto = new OrderGroupedResponse(order);
                    order.getOrderItemList().forEach(dto::addItem);
                    return dto;
                })
                .toList();

        OrderPageResponse response = new OrderPageResponse();
        response.orderList = orderList;
        response.page = pageResult.getNumber() + 1;
        response.size = pageResult.getSize();
        response.totalElements = pageResult.getTotalElements();
        response.totalPages = pageResult.getTotalPages();

        return response;
    }
}
