package com._NT.deliveryShop.repository;

import com._NT.deliveryShop.domain.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.username = :username AND u.isDeleted = false")
    Optional<User> findByUsername(String username);

    @Query("SELECT u FROM User u WHERE u.email = :email AND u.isDeleted = false")
    Optional<User> findByEmail(String email);

    Page<User> findAllByIsDeletedFalse(Pageable pageable);

    @Modifying
    @Query("UPDATE User u SET u.deletedAt = :deletedAt, u.deletedBy = :deletedBy, u.isDeleted = true " +
            "WHERE u.userId = :userId")
    void deleteById(@Param("userId") Long userId, @Param("deletedAt") LocalDateTime deletedAt,
                    @Param("deletedBy") Long deletedBy); // userId : 삭제될 유저, DeleteBy : 삭제하는 유저
}