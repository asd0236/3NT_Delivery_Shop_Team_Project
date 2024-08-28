package com._NT.deliveryShop.repository;

import com._NT.deliveryShop.domain.entity.DeliveryAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<DeliveryAddress, Long> {
}
