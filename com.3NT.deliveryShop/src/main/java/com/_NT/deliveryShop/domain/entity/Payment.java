package com._NT.deliveryShop.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "p_payment")
@Getter
@NoArgsConstructor
public class Payment extends Timestamped {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "payment_id", updatable = false, nullable = false)
    private UUID paymentId;

    // Order와 일대일 관계, 연관관계 주인 설정
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(name = "amount", nullable = false)
    private int amount;

    // 결제 시각
    @Column(name = "payment_time", nullable = false)
    private LocalDateTime paymentTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false)
    private PaymentMethod paymentMethod;

    public Payment(Order order, int amount, LocalDateTime now, PaymentMethod paymentMethod) {
        this.order = order;
        this.amount = amount;
        this.paymentTime = now;
        this.paymentMethod = paymentMethod;
    }

    public void modifyPayment(int amount) {
        this.amount = amount;
    }
}
