package com._NT.deliveryShop.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.With;
import org.hibernate.annotations.UuidGenerator;


@With
@Entity
@Table(name = "p_product")
@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends Timestamped {

    @Id
    @UuidGenerator
    @Column(name = "product_id", updatable = false, nullable = false)
    private UUID productId;

    // 음식점과 다대일 관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "price", nullable = false)
    private Integer price;

    @Column(name = "image_url", nullable = false)
    private String imageURL;

    @Column(name = "isActivated", nullable = false)
    private Boolean isActivated;

    @Builder
    public Product(Restaurant restaurant, String name, String description, Integer price,
        String imageURL, Boolean isActivated) {
        this.restaurant = restaurant;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageURL = imageURL;
        this.isActivated = isActivated;
    }
}
