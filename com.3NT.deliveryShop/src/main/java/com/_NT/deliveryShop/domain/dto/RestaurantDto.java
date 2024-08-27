package com._NT.deliveryShop.domain.dto;

import com._NT.deliveryShop.domain.entity.Restaurant;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.util.Streamable;

public interface RestaurantDto {

    @Data
    @Builder
    class Create {

        private String name;
        private String categoryName;
        private Long ownerId;
        private String mobileNumber;
        private String address;
        private LocalTime businessStartHours;
        private LocalTime businessEndHours;
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
