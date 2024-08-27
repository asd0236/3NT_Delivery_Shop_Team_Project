package com._NT.deliveryShop.domain.dto;

import com._NT.deliveryShop.domain.entity.OrderStatus;
import lombok.Getter;

import java.util.UUID;

@Getter
public class OrderCreateRequestDto {
    private UUID restaurantId;
    private UUID deliveryInfoId;
    private Boolean isOnline;

    public OrderCreateRequestDto(UUID restaurantId, UUID delieveryInfoId, OrderStatus status, Boolean isOnline) {
        this.restaurantId = restaurantId;
        this.deliveryInfoId = delieveryInfoId;
        this.isOnline = isOnline;
    }
}
