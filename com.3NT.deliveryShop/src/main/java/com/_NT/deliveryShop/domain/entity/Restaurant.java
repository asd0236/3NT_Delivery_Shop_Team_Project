package com._NT.deliveryShop.domain.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.With;
import org.hibernate.annotations.GenericGenerator;

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

    // Category와 일대일 관계
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @Column(name = "mobile_number", nullable = false)
    private String mobileNumber;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "business_start_hours", nullable = false)
    private LocalTime businessStartHours;

    @Column(name = "business_end_hours", nullable = false)
    private LocalTime businessEndHours;

    @Column(name = "image_url", nullable = false)
    private String imageURL;
}
