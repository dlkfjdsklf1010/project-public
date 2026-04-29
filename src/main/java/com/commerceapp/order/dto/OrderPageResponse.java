package com.commerceapp.order.dto;

import com.commerceapp.order.entity.Order;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
public class OrderPageResponse {
    private List<OrderSimpleResponse> productList;
    private int page;
    private int size;
    private Long totalElements;
    private int totalPages;

    public static OrderPageResponse from(Page<Order> pageData) {
        OrderPageResponse response = new OrderPageResponse();
        response.productList = pageData.getContent()
                .stream()
                .map(OrderSimpleResponse::new)
                .toList();

        response.page = pageData.getNumber() + 1;
        response.size = pageData.getSize();
        response.totalElements = pageData.getTotalElements();
        response.totalPages = pageData.getTotalPages();

        return response;
    }
}
