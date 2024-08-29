package com._NT.deliveryShop.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Table(name = "p_order")
@Getter
@NoArgsConstructor
public class Order extends Timestamped {

    @Id
    @UuidGenerator
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

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OrderStatus status;

    @Column(name = "is_online", nullable = false)
    private Boolean isOnline;

    @OneToMany(mappedBy = "order")
    private List<OrderProduct> orderProducts;

    public Order(User user, Restaurant restaurant, Payment payment, DeliveryInfo deliveryInfo,
        OrderStatus status, Boolean isOnline) {
        this.user = user;
        this.restaurant = restaurant;
        this.payment = payment;
        this.deliveryInfo = deliveryInfo;
        this.status = status;
        this.isOnline = isOnline;
        // 연관관계 설정
        if (payment != null)
            setPayment(payment);
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
        if (payment.getOrder() != this) {
            payment.setOrder(this);
        }
    }
}
