package com._NT.deliveryShop.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "p_users")
public class User extends Timestamped implements Serializable {

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
    @Enumerated(EnumType.STRING)
    private UserRoleEnum role;

    public User(String username, String password, String email, String mobileNumber, UserRoleEnum role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.mobileNumber = mobileNumber;
        this.role = role;
    }

    public void modifyUser(String username, String password, String email, String mobileNumber, UserRoleEnum role){
        this.username = username;
        this.password = password;
        this.email = email;
        this.mobileNumber = mobileNumber;
        this.role = role;
    }

}
