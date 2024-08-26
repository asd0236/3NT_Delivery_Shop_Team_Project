package com._NT.deliveryShop.domain.entity;

public enum PaymentMethod {
    CREDIT_CARD("credit_card"),
    BANK_TRANSFER("bank_transfer");

    private final String method;

    PaymentMethod(String method) {
        this.method = method;
    }

    public String getMethod() {
        return this.method;
    }

    @Override
    public String toString() {
        return this.method;
    }
}
