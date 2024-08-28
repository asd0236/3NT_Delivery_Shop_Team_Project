package com._NT.deliveryShop.repository;

import com._NT.deliveryShop.domain.entity.Order;
import com._NT.deliveryShop.domain.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
    @Query("SELECT o FROM Order o WHERE o.orderId = :orderId")
    Optional<Order> findByOrderId(UUID orderId);

    @Modifying
    @Query("UPDATE Order o SET o.isDeleted = true, o.deletedAt = :deletedAt, o.deletedBy = :deletedBy " +
            "WHERE o.orderId = :orderId")
    void deleteOrder(UUID orderId, LocalDateTime deletedAt, Long deletedBy);

    @Query("SELECT o FROM Order o WHERE o.restaurant.owner = :user AND o.isDeleted = false")
    Page<Order> findAllByRestaurantOwner(User user, Pageable pageable);

    @Query("SELECT o FROM Order o WHERE o.user = :user AND o.isDeleted = false")
    Page<Order> findAllByUser(User user, Pageable pageable);

}
