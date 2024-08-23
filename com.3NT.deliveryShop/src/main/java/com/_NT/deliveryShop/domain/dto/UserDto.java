package com._NT.deliveryShop.domain.dto;

import com._NT.deliveryShop.domain.entity.UserRoleEnum;
import lombok.Builder;
import lombok.Data;

public interface UserDto {

    @Data
    @Builder
    class Login {
        private String username;
        private String password;
    }

    @Data
    @Builder
    class Create {
        private String username;
        private String password;
        private String email;
        private String mobileNumber;
        private UserRoleEnum role;
    }

    @Data
    @Builder
    class Response {
        private String username;
        private String email;
        private String mobileNumber;
        private UserRoleEnum role;
    }
}