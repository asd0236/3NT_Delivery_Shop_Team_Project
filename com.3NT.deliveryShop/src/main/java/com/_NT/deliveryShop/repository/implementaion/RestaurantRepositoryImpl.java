package com._NT.deliveryShop.repository.implementaion;

import static com._NT.deliveryShop.domain.entity.QRestaurant.restaurant;

import com._NT.deliveryShop.domain.entity.Restaurant;
import com._NT.deliveryShop.repository.custom.RestaurantRepositoryCustom;
import com._NT.deliveryShop.repository.querydsl.PagingUtil;
import com._NT.deliveryShop.repository.searchcondition.RestaurantSearchCondition;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RestaurantRepositoryImpl implements RestaurantRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final PagingUtil pagingUtil;

    @Override
    public PageImpl<Restaurant> search(RestaurantSearchCondition restaurantSearchCondition,
        Pageable pageable) {
        JPAQuery<Restaurant> query = queryFactory
            .selectFrom(restaurant)
            .where(
                likeName(restaurantSearchCondition.nameLike()),
                inCategoryNames(restaurantSearchCondition.categoryNames())
            );

        return pagingUtil.getPageImpl(pageable, query, Restaurant.class);
    }

    private BooleanExpression likeName(String nameLike) {
        if (nameLike == null || nameLike.isEmpty()) {
            return null;
        }
        return restaurant.name.like("%" + nameLike + "%");
    }

    private BooleanExpression inCategoryNames(List<String> categoryNames) {
        return categoryNames.isEmpty() ? null : restaurant.category.name.in(categoryNames);
    }
}