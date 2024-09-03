package com._NT.deliveryShop.domain.dto;

import com._NT.deliveryShop.domain.entity.User;
import com._NT.deliveryShop.domain.entity.UserRoleEnum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
        @NotBlank(message = "유저를 입력해주세요.")
        @Size(min = 3, max = 20, message = "유저 이름은 3자 이상 20자 이하로 입력해주세요.")
        private final String username;

        @NotBlank(message = "비밀번호를 입력해주세요.")
        @Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하로 입력해주세요.")
        @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,20}$", message = "비밀번호는 영문, 숫자, 특수문자를 모두 포함해야 합니다.")
        private final String password;

        @NotBlank(message = "이메일을 입력해주세요.")
        @Email(message = "이메일 형식이 올바르지 않습니다.")
        private final String email;

        @NotBlank(message = "휴대폰 번호를 입력해주세요.")
        @Pattern(regexp = "^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$", message = "휴대폰 번호 형식이 올바르지 않습니다.")
        private final String mobileNumber;

        private final UserRoleEnum role;
    }

    @Data
    @Builder
    final class Modify {
        @NotBlank(message = "유저를 입력해주세요.")
        @Size(min = 3, max = 20, message = "유저 이름은 3자 이상 20자 이하로 입력해주세요.")
        private final String username;

        @NotBlank(message = "비밀번호를 입력해주세요.")
        @Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하로 입력해주세요.")
        @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,20}$", message = "비밀번호는 영문, 숫자, 특수문자를 모두 포함해야 합니다.")
        private final String password;

        @NotBlank(message = "이메일을 입력해주세요.")
        @Email(message = "이메일 형식이 올바르지 않습니다.")
        private final String email;

        @NotBlank(message = "휴대폰 번호를 입력해주세요.")
        @Pattern(regexp = "^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$", message = "휴대폰 번호 형식이 올바르지 않습니다.")
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