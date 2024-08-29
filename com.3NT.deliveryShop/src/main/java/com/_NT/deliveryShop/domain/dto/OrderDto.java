package com._NT.deliveryShop.domain.dto;

import com._NT.deliveryShop.domain.entity.Order;
import com._NT.deliveryShop.domain.entity.OrderProduct;
import com._NT.deliveryShop.domain.entity.OrderStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public interface OrderDto {
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    class CreateOrderRequest {
        private UUID restaurantId;
        private Boolean isOnline;

        public CreateOrderRequest(UUID restaurantId, Boolean isOnline) {
            this.restaurantId = restaurantId;
            this.isOnline = isOnline;
        }
    }

    @Getter
    @NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
    class CreateOrderResponse {
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

        public CreateOrderResponse(Order order) {
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

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    class DeleteResponse {
        private UUID orderId;

        public DeleteResponse(UUID orderId) {
            this.orderId = orderId;
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    class Item {
        private String name;
        private int quantity;
        private int totalPrice;

        public Item(OrderProduct orderProduct) {
            this.name = orderProduct.getProduct().getName();
            this.quantity = orderProduct.getQuantity();
            this.totalPrice = orderProduct.getProduct().getPrice() * orderProduct.getQuantity();
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    class ModifyRequest {
        private OrderStatus status;

        public ModifyRequest(OrderStatus status) {
            this.status = status;
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    class OrderResponse {
        private UUID orderId;
        private String restaurantName;
        private String restaurantMobileNumber;
        private int totalPrice;
        private OrderStatus status;
        private Boolean isOnline;
        private String deliveryAddress;
        private String mobileNumber;
        private PaymentInfoDto paymentInfoDto;
        private List<Item> orderItems = new ArrayList<>();

        public OrderResponse(Order order) {
            this.orderId = order.getOrderId();
            this.restaurantName = order.getRestaurant().getName();
            this.restaurantMobileNumber = order.getRestaurant().getMobileNumber();
            this.status = order.getStatus();
            this.isOnline = order.getIsOnline();
            this.deliveryAddress = order.getAddress();
            this.mobileNumber = order.getMobileNumber();
            this.paymentInfoDto = new PaymentInfoDto(order.getPayment());
            for (OrderProduct orderProduct : order.getOrderProducts()) {
                orderItems.add(new Item(orderProduct));
            }
            this.totalPrice = orderItems.stream()
                    .mapToInt(Item::getTotalPrice)
                    .sum();
        }
    }
}
