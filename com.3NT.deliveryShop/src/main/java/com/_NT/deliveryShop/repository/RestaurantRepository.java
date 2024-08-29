package com._NT.deliveryShop.repository;

import com._NT.deliveryShop.domain.entity.Restaurant;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, UUID> {

    @Modifying
    @Query(
        "UPDATE Restaurant p SET p.isDeleted = true, p.deletedAt = :deletedAt, p.deletedBy = :deletedBy "
            +
            "WHERE p.restaurantId = :restaurantId")
    void softDeleteRestaurant(UUID restaurantId, LocalDateTime deletedAt, Long deletedBy);
}
