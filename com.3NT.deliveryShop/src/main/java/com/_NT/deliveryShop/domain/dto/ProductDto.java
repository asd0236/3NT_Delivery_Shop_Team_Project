package com._NT.deliveryShop.domain.dto;

import com._NT.deliveryShop.domain.entity.Product;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Data;
import lombok.With;
import org.springframework.data.util.Streamable;

public interface ProductDto {

    @With
    @Data
    @Builder
    class Create {

        private final UUID restaurantId;
        private final String name;
        private final String description;
        private final Integer price;
        private final String imageURL;
        private final Boolean isActivated;

        public Product asEntity(
            Function<? super Product, ? extends Product> initialize) {
            return initialize.apply(Product.builder()
                .name(name)
                .description(description)
                .price(price)
                .imageURL(imageURL)
                .isActivated(isActivated)
                .build());
        }
    }

    @With
    @Data
    @Builder
    class Put {

        private final UUID restaurantId;
        private final String name;
        private final String description;
        private final Integer price;
        private final String imageURL;

        public Product asEntity(
            Function<? super Product, ? extends Product> initialize) {
            return initialize.apply(Product.builder()
                .name(name)
                .description(description)
                .price(price)
                .imageURL(imageURL)
                .build());
        }
    }

    @With
    @Data
    @Builder
    class Patch {

        private final UUID restaurantId;
        private final Boolean isActivated;
    }

    @With
    @Data
    @Builder
    class Result {

        private final UUID productId;
        private final UUID restaurantId;
        private final String name;
        private final String description;
        private final Integer price;
        private final String imageURL;
        private final Boolean isActivated;

        public static Result of(Product product) {
            return Result.builder()
                .productId(product.getProductId())
                .restaurantId(product.getRestaurant().getRestaurantId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .imageURL(product.getImageURL())
                .isActivated(product.getIsActivated())
                .build();
        }

        public static List<Result> of(Streamable<Product> products) {
            return products.stream()
                .map(Result::of)
                .collect(Collectors.toList());
        }

        @Data
        @Builder
        public static class Deleted {

            private UUID productId;

            public static Deleted of(Product product) {
                return Deleted.builder()
                    .productId(product.getProductId())
                    .build();
            }
        }
    }
}
