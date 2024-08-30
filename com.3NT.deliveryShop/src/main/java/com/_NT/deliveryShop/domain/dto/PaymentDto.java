package com._NT.deliveryShop.domain.dto;

import com._NT.deliveryShop.domain.entity.Payment;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

public interface PaymentDto {

    @Data
    @Builder
    final class Create {
        private final UUID orderId;
        private final int amount;
    }

    @Data
    @Builder
    final class Response {
        private final UUID paymentId;
        private final UUID orderId;
        private final int amount;
        private final LocalDateTime paymentTime;
        private final String paymentResult;

        public static Response of(Payment payment) {
            return Response.builder()
                    .paymentId(payment.getPaymentId())
                    .orderId(payment.getOrder().getOrderId())
                    .amount(payment.getAmount())
                    .paymentTime(payment.getPaymentTime())
                    .paymentResult("외부 API 사용")
                    .build();
        }
    }

    @Data
    @NoArgsConstructor(force = true)
    final class GetAllPaymentsResponse {
        private final UUID paymentId;
        private final UUID orderId;
        private final int amount;
        private final LocalDateTime paymentTime;
        private final String paymentResult;

        public GetAllPaymentsResponse(Payment payment) {
            this.paymentId = payment.getPaymentId();
            this.orderId = payment.getOrder().getOrderId();
            this.amount = payment.getAmount();
            this.paymentTime = payment.getPaymentTime();
            paymentResult = "외부 API 사용";
        }
    }

    @Data
    @Builder
    final class DeletePaymentResult {
        private final UUID paymentId;
    }
}
