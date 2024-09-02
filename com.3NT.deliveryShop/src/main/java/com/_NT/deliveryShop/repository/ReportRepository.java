package com._NT.deliveryShop.repository;

import com._NT.deliveryShop.domain.entity.Report;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

    @Modifying
    @Query(
        "UPDATE Report p SET p.isDeleted = true, p.deletedAt = :deletedAt, p.deletedBy = :deletedBy "
            +
            "WHERE p.id = :id")
    void softDeleteReport(UUID id, LocalDateTime deletedAt, Long deletedBy);

    Page<Report> findAllByIsDeletedFalse(Pageable pageable);

    Page<Report> findAllByOwnerUserIdAndIsDeletedFalse(Long ownerUserId, Pageable pageable);
}
