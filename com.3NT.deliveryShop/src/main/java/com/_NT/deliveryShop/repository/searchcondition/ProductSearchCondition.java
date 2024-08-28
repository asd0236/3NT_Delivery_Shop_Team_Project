package com._NT.deliveryShop.repository.searchcondition;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


@Data
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public final class ProductSearchCondition {

    private final String nameLike;
}
