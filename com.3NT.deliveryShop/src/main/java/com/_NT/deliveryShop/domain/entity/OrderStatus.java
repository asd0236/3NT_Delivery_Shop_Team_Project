package com._NT.deliveryShop.domain.entity;

/**
 * 주문 상태를 나타내는 Enum 클래스
 * 상태는 결재 중, 결제 취소, 결제 완료, 배송 준비, 배송 중, 배송 완료, 주문 취소가 있다.
 * 각 상태는 문자열로 표현된다.
 */
public enum OrderStatus {
    PAYMENT_PENDING("payment_pending"),
    PAYMENT_CANCELED("payment_canceled"),
    PAYMENT_COMPLETED("payment_completed"),
    DELIVERY_PREPARING("delivery_preparing"),
    DELIVERING("delivering"),
    DELIVERED("delivered"),
    CANCELED("ceanceled");


    private final String status;

    OrderStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return this.status;
    }

    @Override
    public String toString() {
        return this.status;
    }
}
