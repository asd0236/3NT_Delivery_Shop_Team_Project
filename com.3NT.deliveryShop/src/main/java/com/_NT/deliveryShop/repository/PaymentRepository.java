package com._NT.deliveryShop.repository;

import com._NT.deliveryShop.domain.entity.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, UUID> {

    Page<Payment> findAllByIsDeletedFalse(Pageable pageable);

    @Query("SELECT p FROM Payment p WHERE p.order.user.userId = :userId AND p.isDeleted = false")
    Page<Payment> findAllNotDeletedForUser(@Param("userId") Long userId, Pageable pageable);

    @Modifying
    @Query("UPDATE Payment p SET p.deletedAt = :deletedAt, p.deletedBy = :deletedBy, p.isDeleted = true " +
            "WHERE p.paymentId = :paymentId")
    void deleteById(@Param("paymentId") UUID paymentId, @Param("deletedAt")LocalDateTime deletedAt
            , @Param("deletedBy")Long deletedBy);
}
