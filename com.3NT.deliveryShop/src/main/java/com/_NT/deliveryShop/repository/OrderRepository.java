package com._NT.deliveryShop.repository;

import com._NT.deliveryShop.domain.entity.Order;
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
    @Query("UPDATE Order o SET o.isDeleted = true, o.deletedAt = :deletedAt, o.deletedBy = :deletedBy WHERE o.orderId = :orderId")
    void deleteOrder(UUID orderId, LocalDateTime deletedAt, Long deletedBy);
}
