package com._NT.deliveryShop.domain.dto;

import com._NT.deliveryShop.domain.entity.Category;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;

public interface CategoryDto {

    @Data
    @Builder
    final class Create {

        private final String name;

        public Category asEntity() {
            return Category.builder()
                .name(name)
                .build();
        }
    }

    @Data
    @Builder
    final class Put {

        private final String name;

    }

    @Data
    @Builder
    final class Result {

        private final UUID categoryId;
        private final String name;

        public static Result of(Category category) {
            return Result.builder()
                .categoryId(category.getCategoryId())
                .name(category.getName())
                .build();
        }
    }
}
