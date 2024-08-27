package com._NT.deliveryShop.domain.dto;

import com._NT.deliveryShop.domain.entity.DeliveryInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class DeliveryInfoDto {
    private UUID deliveryInfoId;
    private String address;
    private String mobileNumber;

    public DeliveryInfoDto(DeliveryInfo deliveryInfo) {
        this.deliveryInfoId = deliveryInfo.getDeliveryInfoId();
        this.address = deliveryInfo.getDeliveryAddress().getAddress();
        this.mobileNumber = deliveryInfo.getMobileNumber();
    }
}
