package com._NT.deliveryShop.domain.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

public interface DeliveryAddressDto {
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    class CreateAddressRequest {
        private String address;
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    class AddressResponse {
        private Long userId;
        private UUID deliveryAdressId;
        private String address;

        public AddressResponse(Long userId, UUID deliveryId, String address) {
            this.userId = userId;
            this.deliveryAdressId = deliveryId;
            this.address = address;
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    class DeleteAddressResponse {
        private UUID deliveryAddressId;

        public DeleteAddressResponse(UUID deliveryAddressId) {
            this.deliveryAddressId = deliveryAddressId;
        }
    }
}
