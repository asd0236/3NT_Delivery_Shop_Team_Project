package com._NT.deliveryShop.domain.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Table(name = "p_delivery_info")
@Getter
@NoArgsConstructor
public class DeliveryInfo extends Timestamped {

    @Id
    @UuidGenerator
    @Column(name = "delivery_info_id", updatable = false, nullable = false)
    private UUID deliveryInfoId;

    // delivery_address와 다대 일 관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_address_id", nullable = false)
    private DeliveryAddress deliveryAddress;

    // Order과 일대일 관계, FK는 order 테이블에 있음
    @OneToOne(mappedBy = "deliveryInfo")
    private Order order;

    @Column(name = "mobile_number", nullable = false)
    private String mobileNumber;

    @Column(name = "request_note")
    private String requestNote;
}

