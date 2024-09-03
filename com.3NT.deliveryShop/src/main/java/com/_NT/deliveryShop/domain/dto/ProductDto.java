package com._NT.deliveryShop.domain.dto;

import com._NT.deliveryShop.domain.entity.Product;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.With;
import org.springframework.data.util.Streamable;

public interface ProductDto {

    @With
    @Data
    @Builder
    class Create {

        @NotNull(message = "식당 ID는 필수 항목입니다.")
        private final UUID restaurantId;

        @NotBlank(message = "상품 이름을 입력해주세요.")
        @Size(min = 1, max = 100, message = "상품 이름은 1자 이상 100자 이하로 입력해 주세요.")
        private final String name;

        @Size(max = 500, message = "상품 설명은 500자 이하로 입력해 주세요.")
        private final String description;

        @NotNull(message = "가격을 입력해 주세요.")
        @Positive(message = "가격은 0보다 커야 합니다.")
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

        @NotNull(message = "식당 ID는 필수 항목입니다.")
        private final UUID restaurantId;

        @NotBlank(message = "상품 이름을 입력해주세요.")
        @Size(min = 1, max = 100, message = "상품 이름은 1자 이상 100자 이하로 입력해 주세요.")
        private final String name;

        @Size(max = 500, message = "상품 설명은 500자 이하로 입력해 주세요.")
        private final String description;

        @NotNull(message = "가격을 입력해 주세요.")
        @Positive(message = "가격은 0보다 커야 합니다.")
        private final Integer price;

        private final String imageURL;

        public Product asPutEntity(Product product) {
            product.setName(name);
            product.setDescription(description);
            product.setPrice(price);
            product.setImageURL(imageURL);
            return product;
        }
    }

    @With
    @Data
    @Builder
    class Patch {

        private final UUID restaurantId;
        private final Boolean isActivated;
        private final String imageURL;
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
