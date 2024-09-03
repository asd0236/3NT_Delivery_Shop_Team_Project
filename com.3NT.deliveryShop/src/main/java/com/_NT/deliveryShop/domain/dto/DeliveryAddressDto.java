package com._NT.deliveryShop.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

public interface DeliveryAddressDto {
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    class CreateAddressRequest {
        @NotBlank(message = "배송지 주소는 필수 입력값입니다.")
        @Pattern(regexp = "^[가-힣a-zA-Z0-9\\s]*$", message = "특수문자는 입력할 수 없습니다.")
        @Size(max = 255, message = "주소는 255자 이하로 입력해 주세요.")
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

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    class ModifyAddressRequest {
        @NotBlank(message = "배송지 주소는 필수 입력값입니다.")
        @Pattern(regexp = "^[가-힣a-zA-Z0-9\\s]*$", message = "특수문자는 입력할 수 없습니다.")
        @Size(max = 255, message = "주소는 255자 이하로 입력해 주세요.")
        private String address;
    }

}
