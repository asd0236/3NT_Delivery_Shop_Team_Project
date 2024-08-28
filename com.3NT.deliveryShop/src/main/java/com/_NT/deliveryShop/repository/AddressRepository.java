package com._NT.deliveryShop.repository;

import com._NT.deliveryShop.domain.entity.DeliveryAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<DeliveryAddress, Long> {
    @Query("SELECT da FROM DeliveryAddress da WHERE da.user.userId = :userId AND da.isDeleted = false")
    List<DeliveryAddress> findAllByUserId(Long userId);
}
