package com._NT.deliveryShop.repository.implementaion;

import static com._NT.deliveryShop.domain.entity.QProduct.product;

import com._NT.deliveryShop.domain.entity.Product;
import com._NT.deliveryShop.repository.custom.ProductRepositoryCustom;
import com._NT.deliveryShop.repository.querydsl.PagingUtil;
import com._NT.deliveryShop.repository.searchcondition.ProductSearchCondition;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final PagingUtil pagingUtil;

    @Override
    public PageImpl<Product> search(ProductSearchCondition productSearchCondition,
        Pageable pageable) {
        JPAQuery<Product> query = queryFactory
            .selectFrom(product)
            .where(
                isNotDeleted(),
                likeName(productSearchCondition.getNameLike())
            );

        return pagingUtil.getPageImpl(pageable, query, Product.class);
    }

    private BooleanExpression isNotDeleted() {
        return product.isDeleted.isFalse();
    }

    private BooleanExpression likeName(String nameLike) {
        if (nameLike == null || nameLike.isEmpty()) {
            return null;
        }
        return product.name.like("%" + nameLike + "%");
    }
}