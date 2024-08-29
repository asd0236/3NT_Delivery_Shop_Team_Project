package com._NT.deliveryShop.repository;

import com._NT.deliveryShop.domain.entity.Product;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {

    @Modifying
    @Query(
        "UPDATE Product p SET p.isDeleted = true, p.deletedAt = :deletedAt, p.deletedBy = :deletedBy "
            +
            "WHERE p.productId = :productId")
    void softDeleteProduct(UUID productId, LocalDateTime deletedAt, Long deletedBy);

    Page<Product> findAllByIsDeletedFalse(Pageable pageable);
}
