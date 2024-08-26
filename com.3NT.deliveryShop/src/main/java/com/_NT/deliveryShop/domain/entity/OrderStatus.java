package com._NT.deliveryShop.domain.entity;

public enum OrderStatus {
    PREPARING("preparing"),
    ON_DELIVERY("on_delivery"),
    COMPLETED("completed"),
    CANCELED("canceled");


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
