package com.commerceapp.order.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class OrderItemRequest {
    @NotBlank(message = "상품 번호는 필수 입력입니다.")
    private Long productId;

    @NotBlank(message = "수량을 입력해주세요.")
    private int quantity;
}
