package com._NT.deliveryShop.domain.dto;

import com._NT.deliveryShop.domain.entity.Order;
import com._NT.deliveryShop.domain.entity.OrderProduct;
import com._NT.deliveryShop.domain.entity.OrderStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com._NT.deliveryShop.domain.dto.OrderProductDto.*;

public interface OrderDto {
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    class CreateOrderRequest {
        @NotNull(message = "식당 ID는 필수 항목입니다.")
        private UUID restaurantId;

        @NotNull(message = "주문 상품은 필수 항목입니다.")
        private List<CreateOrderProduct> orderItems;

        @NotBlank(message = "배달 주소를 입력해주세요.")
        @Size(max = 255, message = "배달 주소는 255자 이하로 입력해주세요.")
        private String deliveryAddress;

        @NotBlank(message = "연락처를 입력해주세요.")
        @Pattern(regexp = "^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$", message = "연락처 형식이 올바르지 않습니다.")
        private String mobileNumber;

        private boolean isOnline;
    }

    @Getter
    @NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
    class CreateOrderResponse {
        private UUID orderId;
        private Long userId;
        private UUID restaurantId;
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
        private UUID productId;
        private String name;
        private int quantity;
        private int price;

        public Item(OrderProduct orderProduct) {
            this.productId = orderProduct.getProduct().getProductId();
            this.name = orderProduct.getProduct().getName();
            this.quantity = orderProduct.getQuantity();
            this.price = orderProduct.getProduct().getPrice();
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    class ModifyRequest {
        @NotNull(message = "주문 상태를 입력해주세요.")
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
            this.totalPrice = orderItems.stream().mapToInt(item -> item.getPrice() * item.getQuantity()).sum();
        }
    }
}
