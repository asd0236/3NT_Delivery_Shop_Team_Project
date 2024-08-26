package com._NT.deliveryShop.domain.dto;

import com._NT.deliveryShop.domain.entity.User;
import com._NT.deliveryShop.domain.entity.UserRoleEnum;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public interface UserDto {

    @Data
    @NoArgsConstructor
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
    class Modify {
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


    @Data
    @NoArgsConstructor
    class GetAllUsersResponse {
        private Long userId;
        private String username;
        private String email;
        private String mobileNumber;
        private UserRoleEnum role;

        public GetAllUsersResponse(User user) {
            this.userId = user.getUserId();
            this.username = user.getUsername();
            this.email = user.getEmail();
            this.mobileNumber = user.getMobileNumber();
            this.role = user.getRole();
        }
    }

    @Data
    @Builder
    class ModifyUserResponse {
        private Long userId;
        private String username;
        private String email;
        private String mobileNumber;
        private UserRoleEnum role;
    }
}