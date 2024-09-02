package com._NT.deliveryShop.repository;

import com._NT.deliveryShop.domain.entity.Notice;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Long> {

    @Modifying
    @Query(
        "UPDATE Notice p SET p.isDeleted = true, p.deletedAt = :deletedAt, p.deletedBy = :deletedBy "
            +
            "WHERE p.id = :id")
    void softDeleteNotice(UUID id, LocalDateTime deletedAt, Long deletedBy);

    Page<Notice> findAllByIsDeletedFalse(Pageable pageable);
}
