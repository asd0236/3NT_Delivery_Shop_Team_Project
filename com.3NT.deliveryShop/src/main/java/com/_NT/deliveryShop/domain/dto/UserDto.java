package com._NT.deliveryShop.domain.dto;

import com._NT.deliveryShop.domain.entity.User;
import com._NT.deliveryShop.domain.entity.UserRoleEnum;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public interface UserDto {

    @Data
    @NoArgsConstructor
    final class Login {
        private String username;
        private String password;
    }

    @Data
    @Builder
    final class Create {
        private final String username;
        private final String password;
        private final String email;
        private final String mobileNumber;
        private final UserRoleEnum role;
    }

    @Data
    @Builder
    final class Modify {
        private final String username;
        private final String password;
        private final String email;
        private final String mobileNumber;
        private final UserRoleEnum role;
    }

    @Data
    @Builder
    final class Result {
        private final String username;
        private final String email;
        private final String mobileNumber;
        private final UserRoleEnum role;

        public static Result of(User user) {
            return Result.builder()
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .mobileNumber(user.getMobileNumber())
                    .role(user.getRole())
                    .build();
        }
    }


    @Data
    @NoArgsConstructor
    final class GetAllUsersResponse {
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
    final class ModifyUserResult {
        private final Long userId;
        private final String username;
        private final String email;
        private final String mobileNumber;
        private final UserRoleEnum role;

        public static ModifyUserResult of(User user) {
            return ModifyUserResult.builder()
                    .userId(user.getUserId())
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .mobileNumber(user.getMobileNumber())
                    .role(user.getRole())
                    .build();
        }
    }

    @Data
    @Builder
    final class DeleteUserResult {
        private final Long userId;
    }
}