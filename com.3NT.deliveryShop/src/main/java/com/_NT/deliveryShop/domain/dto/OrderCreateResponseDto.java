package com._NT.deliveryShop.domain.dto;

import com._NT.deliveryShop.domain.entity.Order;
import com._NT.deliveryShop.domain.entity.OrderStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderCreateResponseDto {
    private UUID orderId;
    private Long userId;
    private UUID restaurantId;
    private UUID paymentId;
    private OrderStatus status;
    private Boolean isOnline;
    private LocalDateTime createdAt;
    private Long createBy;
    private LocalDateTime updatedAt;
    private Long updateBy;

    public OrderCreateResponseDto(Order order) {
        this.orderId = order.getOrderId();
        this.userId = order.getUser().getUserId();
        this.restaurantId = order.getRestaurant().getRestaurantId();
        this.paymentId = order.getPayment().getPaymentId();
        this.status = order.getStatus();
        this.isOnline = order.getIsOnline();
        this.createdAt = order.getCreatedAt();
        this.createBy = order.getCreateBy();
        this.updatedAt = order.getUpdatedAt();
        this.updateBy = order.getUpdateBy();
    }
}
