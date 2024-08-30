package com._NT.deliveryShop.repository;

import com._NT.deliveryShop.domain.entity.Category;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {

    @Modifying
    @Query(
        "UPDATE Category c SET c.isDeleted = true, c.deletedAt = :deletedAt, c.deletedBy = :deletedBy "
            +
            "WHERE c.categoryId = :categoryId")
    void softDeleteCategory(UUID categoryId, LocalDateTime deletedAt, Long deletedBy);
}
