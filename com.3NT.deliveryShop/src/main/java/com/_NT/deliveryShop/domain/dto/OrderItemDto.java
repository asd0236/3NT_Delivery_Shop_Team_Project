package com._NT.deliveryShop.domain.dto;

import com._NT.deliveryShop.domain.entity.OrderProduct;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderItemDto {
    private String name;
    // 해당 상품의 수량
    private int quantity;
    // 해당 상품의 총 가격
    private int totalPrice;

    public OrderItemDto(OrderProduct orderItem) {
        this.name = orderItem.getProduct().getName();
        this.quantity = orderItem.getQuantity();
        this.totalPrice = orderItem.getProduct().getPrice() * orderItem.getQuantity();
    }
}
