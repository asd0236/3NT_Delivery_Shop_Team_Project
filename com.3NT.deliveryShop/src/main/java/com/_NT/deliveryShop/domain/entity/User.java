package com._NT.deliveryShop.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "p_users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long userId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String mobileNumber;

    @Column(nullable = false)
    private UserRoleEnum role;

    public User(String username, String password, String email, String mobileNumber, UserRoleEnum role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.mobileNumber = mobileNumber;
        this.role = role;
    }

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDate createdAt;
    private Long createdBy;

    @UpdateTimestamp
    private LocalDate updatedAt;
    private Long updatedBy;

    private LocalDate deletedAt;
    private Long deletedBy;

    private Boolean isDeleted;

}
