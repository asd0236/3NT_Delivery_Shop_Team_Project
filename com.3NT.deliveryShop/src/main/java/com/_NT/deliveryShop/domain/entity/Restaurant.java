package com._NT.deliveryShop.domain.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "p_restaurant")
public class Restaurant extends Timestamped {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "restaurant_id", updatable = false, nullable = false)
    private UUID restaurantId;

    // Catgeory와 일대일 관계
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "owner_id", nullable = false)
    private UUID ownerId;

    @Column(name = "mobile_number", nullable = false)
    private String mobileNumber;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "business_start_hours", nullable = false)
    private LocalDateTime businessStartHours;

    @Column(name = "business_end_hours", nullable = false)
    private LocalDateTime businessEndHours;

    @Column(name = "imageURL", nullable = false)
    private String imageURL;
}
