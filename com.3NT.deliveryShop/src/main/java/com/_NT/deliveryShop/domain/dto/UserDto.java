package com._NT.deliveryShop.domain.dto;

import lombok.Builder;
import lombok.Data;

public interface UserDto {

    @Data
    @Builder
    class Login {
        private String username;
        private String password;
    }

}