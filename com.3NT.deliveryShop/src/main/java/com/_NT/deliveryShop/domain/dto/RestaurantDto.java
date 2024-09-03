package com._NT.deliveryShop.domain.dto;

import com._NT.deliveryShop.domain.entity.Restaurant;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.util.Streamable;

public interface RestaurantDto {

    @Data
    @Builder
    class Create {

        @NotBlank(message = "음식점 이름을 입력해주세요.")
        private String name;

        @NotBlank(message = "음식점 카테고리를 입력해주세요.")
        private String categoryName;

        @NotNull(message = "음식점 소유자를 입력해주세요.")
        private Long ownerId;

        @NotBlank(message = "전화번호를 입력해주세요.")
        @Pattern(regexp = "^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$", message = "전화번호 형식이 올바르지 않습니다.")
        private String mobileNumber;

        @NotBlank(message = "주소를 입력해주세요.")
        private String address;

        @NotNull(message = "영업 시작 시간을 입력해주세요.")
        private LocalTime businessStartHours;

        @NotNull(message = "영업 종료 시간을 입력해주세요.")
        private LocalTime businessEndHours;

        @NotNull(message = "이미지를 입력해주세요.")
        private String imageURL;


        public Restaurant asEntity(
            Function<? super Restaurant, ? extends Restaurant> initialize) {
            return initialize.apply(Restaurant.builder()
                .name(name)
                .mobileNumber(mobileNumber)
                .address(address)
                .businessStartHours(businessStartHours)
                .businessEndHours(businessEndHours)
                .imageURL(imageURL)
                .build());
        }
    }

    @Data
    @Builder
    class Update {
        private String name;
        private String categoryName;
        private String mobileNumber;
        private String address;
        private LocalTime businessStartHours;
        private LocalTime businessEndHours;
        private String imageURL;
    }

    @Data
    @Builder
    class Result {

        private UUID restaurantId;
        private String name;
        private String categoryName;
        private String mobileNumber;
        private String address;
        private LocalTime businessStartHours;
        private LocalTime businessEndHours;
        private String imageURL;

        public static Result of(Restaurant restaurant) {
            return Result.builder()
                .restaurantId(restaurant.getRestaurantId())
                .name(restaurant.getName())
                .categoryName(restaurant.getCategory().getName())
                .mobileNumber(restaurant.getMobileNumber())
                .address(restaurant.getAddress())
                .businessStartHours(restaurant.getBusinessStartHours())
                .businessEndHours(restaurant.getBusinessEndHours())
                .imageURL(restaurant.getImageURL())
                .build();
        }

        public static List<Result> of(Streamable<Restaurant> restaurants) {
            return restaurants.stream()
                .map(Result::of)
                .collect(Collectors.toList());
        }

        @Data
        @Builder
        public static class Deleted {

            private UUID restaurantId;

            public static Deleted of(Restaurant restaurant) {
                return Deleted.builder()
                    .restaurantId(restaurant.getRestaurantId())
                    .build();
            }
        }

    }


}
