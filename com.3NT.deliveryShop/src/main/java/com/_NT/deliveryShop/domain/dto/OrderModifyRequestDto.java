package com._NT.deliveryShop.domain.dto;

import com._NT.deliveryShop.domain.entity.OrderStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderModifyRequestDto {
    private OrderStatus status;

    public OrderModifyRequestDto(OrderStatus status) {
        this.status = status;
    }
}
