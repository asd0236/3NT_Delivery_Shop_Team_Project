package com._NT.deliveryShop.domain.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderDeleteResponseDto {
    private UUID orderId;

    public OrderDeleteResponseDto(UUID orderId) {
        this.orderId = orderId;
    }
}
