package com._NT.deliveryShop.domain.dto;

import com._NT.deliveryShop.domain.entity.Order;
import com._NT.deliveryShop.domain.entity.OrderProduct;
import com._NT.deliveryShop.domain.entity.OrderStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
public class OrderResponseDto {
    private UUID orderId;
    private String restaurantName;
    private String restaurantMobileNumber;
    private int totalPrice;
    private OrderStatus status;
    private Boolean isOnline;
    private DeliveryInfoDto deliveryInfo;
    private PaymentInfoDto paymentInfoDto;

    private List<OrderItemDto> orderItems = new ArrayList<>();

    public OrderResponseDto(Order order) {
        this.orderId = order.getOrderId();
        this.restaurantName = order.getRestaurant().getName();
        this.restaurantMobileNumber = order.getRestaurant().getMobileNumber();
        this.status = order.getStatus();
        this.isOnline = order.getIsOnline();
        this.deliveryInfo = new DeliveryInfoDto(order.getDeliveryInfo());
        this.paymentInfoDto = new PaymentInfoDto(order.getPayment());
        for (OrderProduct orderProduct : order.getOrderProducts()) {
            orderItems.add(new OrderItemDto(orderProduct));
        }
        // orderItems 에서 totalPrice 계산하기
        this.totalPrice = orderItems.stream()
                .mapToInt(OrderItemDto::getTotalPrice)
                .sum();
    }
}
