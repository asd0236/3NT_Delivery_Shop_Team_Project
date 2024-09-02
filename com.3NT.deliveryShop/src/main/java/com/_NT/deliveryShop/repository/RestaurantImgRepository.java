package com._NT.deliveryShop.repository;

import com._NT.deliveryShop.domain.entity.RestaurantImg;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantImgRepository extends JpaRepository<RestaurantImg, UUID> {

    Optional<RestaurantImg> findByRestaurantRestaurantIdAndIsDeletedFalse(UUID restaurantId);

    boolean existsByRestaurantRestaurantIdAndIsDeletedFalse(UUID restaurantId);

    @Modifying
    @Query(
        "UPDATE RestaurantImg p SET p.isDeleted = true, p.deletedAt = :deletedAt, p.deletedBy = :deletedBy "
            +
            "WHERE p.uploadFileId = :uploadFileId")
    void softDeleteByUploadFileId(UUID uploadFileId, LocalDateTime deletedAt, Long deletedBy);
}
