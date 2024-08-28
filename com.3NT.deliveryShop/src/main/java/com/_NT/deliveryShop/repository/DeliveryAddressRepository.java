package com._NT.deliveryShop.repository;

import com._NT.deliveryShop.domain.entity.DeliveryAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface DeliveryAddressRepository extends JpaRepository<DeliveryAddress, UUID> {
    @Query("SELECT da FROM DeliveryAddress da WHERE da.user.userId = :userId AND da.isDeleted = false")
    List<DeliveryAddress> findAllByUserId(Long userId);

    @Modifying
    @Query("UPDATE DeliveryAddress da SET da.isDeleted = true, da.deletedAt = :deletedAt, da.deletedBy = :deletedBy " +
            "WHERE da.deliveryAddressId = :addressId")
    void deleteAddress(UUID addressId, Long deletedBy, LocalDateTime deletedAt);

}
