package com._NT.deliveryShop.repository;

import com._NT.deliveryShop.domain.entity.AiChat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AiChatRepository extends JpaRepository<AiChat, UUID> {
    @Query("SELECT a FROM AiChat a WHERE a.user.userId = :userId AND a.isDeleted = false")
    Page<AiChat> findChatForAUser(@Param("userId") Long userId, Pageable pageable);

}
