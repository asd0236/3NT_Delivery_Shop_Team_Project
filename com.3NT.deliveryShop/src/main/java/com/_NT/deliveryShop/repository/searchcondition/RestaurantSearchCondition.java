package com._NT.deliveryShop.repository.searchcondition;

import java.util.ArrayList;
import java.util.List;
import lombok.Builder;

public record RestaurantSearchCondition(String nameLike, List<String> categoryNames) {

    @Builder
    public RestaurantSearchCondition(String nameLike, List<String> categoryNames) {
        this.nameLike = nameLike;
        this.categoryNames = categoryNames == null ? new ArrayList<>() : categoryNames;
    }
}
