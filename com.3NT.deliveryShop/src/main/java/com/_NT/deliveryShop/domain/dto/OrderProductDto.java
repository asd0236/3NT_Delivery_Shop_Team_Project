package com._NT.deliveryShop.domain.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

public interface OrderProductDto {
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    class CreateOrderProduct {
        private UUID productId;
        private String name;
        private Integer quantity;
        private Integer price;
    }
}


