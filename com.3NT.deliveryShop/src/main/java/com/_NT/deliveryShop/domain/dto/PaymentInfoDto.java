package com._NT.deliveryShop.domain.dto;

import com._NT.deliveryShop.domain.entity.Payment;
import com._NT.deliveryShop.domain.entity.PaymentMethod;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor
public class PaymentInfoDto {
    private UUID paymentInfoId;
    private PaymentMethod paymentMethod;
    private LocalDateTime paymentTime;

    public PaymentInfoDto(Payment payment) {
        this.paymentInfoId = payment.getPaymentId();
        this.paymentMethod = payment.getPaymentMethod();
        this.paymentTime = payment.getPaymentTime();
    }
}
