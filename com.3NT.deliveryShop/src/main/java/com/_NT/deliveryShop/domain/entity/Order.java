package com._NT.deliveryShop.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "p_order")
@Getter
@NoArgsConstructor
public class Order extends Timestamped {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "order_id", updatable = false, nullable = false)
    private UUID orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    // Payment와 일대일 관계, Payment 테이블에서 FK 관리
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "order")
    private Payment payment;

    // DeliveryInfo와 일대일 관계, Order 테이블에서 FK 관리
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_info_id", nullable = false)
    private DeliveryInfo deliveryInfo;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OrderStatus status;

    @Column(name = "is_online", nullable = false)
    private Boolean isOnline;

    @OneToMany(mappedBy = "order")
    private List<OrderProduct> orderProducts;

    public Order(User user, Restaurant restaurant, Payment payment, DeliveryInfo deliveryInfo, OrderStatus status, Boolean isOnline) {
        this.user = user;
        this.restaurant = restaurant;
        this.payment = payment;
        this.deliveryInfo = deliveryInfo;
        this.status = status;
        this.isOnline = isOnline;
    }
}
