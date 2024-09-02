package com._NT.deliveryShop.repository;

import com._NT.deliveryShop.domain.entity.Answer;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {

    @Modifying
    @Query(
        "UPDATE Answer p SET p.isDeleted = true, p.deletedAt = :deletedAt, p.deletedBy = :deletedBy "
            +
            "WHERE p.id = :id")
    void softDeleteAnswer(UUID id, LocalDateTime deletedAt, Long deletedBy);

    Page<Answer> findAllByIsDeletedFalse(Pageable pageable);

    Page<Answer> findAllByOwnerUserIdAndIsDeletedFalse(Long ownerUserId, Pageable pageable);

    Answer findByReportIdAndIsDeletedFalse(UUID reportId);
}
