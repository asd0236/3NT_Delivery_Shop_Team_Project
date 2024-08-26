package com._NT.deliveryShop.domain.entity;

public enum UserRoleEnum {
    USER(Authority.USER),  // 사용자 권한
    OWNER(Authority.OWNER), // 가게 사장님 권한
    ADMIN(Authority.ADMIN);  // 관리자 권한

    private final String authority;

    UserRoleEnum(String authority) {
        this.authority = authority;
    }

    public String getAuthority() {
        return this.authority;
    }

    public static class Authority {
        public static final String USER = "ROLE_USER";
        public static final String OWNER = "ROLE_OWNER";
        public static final String ADMIN = "ROLE_ADMIN";
    }
}