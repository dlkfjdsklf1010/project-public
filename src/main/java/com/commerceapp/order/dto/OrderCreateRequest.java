package com.commerceapp.order.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.util.List;

@Getter
public class OrderCreateRequest {
    @NotBlank(message = "고객 번호는 필수 입력입니다.")
    private Long customerId;

    private List<OrderItemRequest> itemList;
}
