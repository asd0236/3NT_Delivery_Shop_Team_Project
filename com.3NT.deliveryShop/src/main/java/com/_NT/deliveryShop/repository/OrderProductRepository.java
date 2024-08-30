package com._NT.deliveryShop.repository;

import com._NT.deliveryShop.domain.entity.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrderProductRepository extends JpaRepository<OrderProduct, UUID> {
}
