package com._NT.deliveryShop.repository.querydsl;

import static java.lang.Math.max;
import static java.lang.Math.min;

import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.core.types.dsl.PathBuilderFactory;
import com.querydsl.jpa.JPQLQuery;
import jakarta.persistence.EntityManager;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.Querydsl;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PagingUtil {

    private final EntityManager em;

    public static <T> List<T> getPage(List<T> entity, Pageable pageable) {
        int maxStartIndex = entity.size() - (int) pageable.getOffset();
        int startIndex = (int) min(pageable.getOffset(), entity.size());
        int lastIndex = (int) pageable.getOffset() + pageable.getPageSize();
        int maxLastIndex = max(startIndex, maxStartIndex);
        lastIndex = min(lastIndex, maxLastIndex);
        return entity.subList(startIndex, lastIndex);
    }

    private Querydsl getQuerydsl(Class<?> clazz) {
        PathBuilder<?> builder = new PathBuilderFactory().create(clazz);
        return new Querydsl(em, builder);
    }

    public <T> PageImpl<T> getPageImpl(Pageable pageable, JPQLQuery<T> query, Class<?> clazz) {
        long totalCount = query.fetch().size();
        List<T> results = getQuerydsl(clazz).applyPagination(pageable, query).fetch();
        return new PageImpl<>(results, pageable, totalCount);
    }
}