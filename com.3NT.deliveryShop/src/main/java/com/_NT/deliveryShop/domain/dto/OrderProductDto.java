package com._NT.deliveryShop.domain.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

public interface OrderProductDto {
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    class CreateOrderProduct {
        @NotNull(message = "상품 ID는 필수 항목입니다.")
        private UUID productId;

        @NotNull(message = "상품 이름은 필수 항목입니다.")
        private String name;

        @NotNull(message = "수량은 필수 항목입니다.")
        @Min(value = 1, message = "수량은 1개 이상이어야 합니다.")
        private Integer quantity;

        @NotNull(message = "가격은 필수 항목입니다.")
        @Min(value = 1, message = "가격은 1원 이상이어야 합니다.")
        private Integer price;
    }
}


