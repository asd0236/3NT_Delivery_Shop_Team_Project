package com._NT.deliveryShop.repository.custom;

import com._NT.deliveryShop.domain.entity.Restaurant;
import com._NT.deliveryShop.repository.searchcondition.RestaurantSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RestaurantRepositoryCustom {

    Page<Restaurant> search(RestaurantSearchCondition restaurantSearchCondition, Pageable pageable);
}

