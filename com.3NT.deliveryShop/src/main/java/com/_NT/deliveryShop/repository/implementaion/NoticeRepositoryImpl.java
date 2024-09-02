package com._NT.deliveryShop.repository.implementaion;

import static com._NT.deliveryShop.domain.entity.QNotice.notice;

import com._NT.deliveryShop.domain.entity.Notice;
import com._NT.deliveryShop.repository.custom.NoticeRepositoryCustom;
import com._NT.deliveryShop.repository.querydsl.PagingUtil;
import com._NT.deliveryShop.repository.searchcondition.NoticeSearchCondition;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NoticeRepositoryImpl implements NoticeRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final PagingUtil pagingUtil;

    @Override
    public PageImpl<Notice> search(NoticeSearchCondition noticeSearchCondition,
        Pageable pageable) {
        JPAQuery<Notice> query = queryFactory
            .selectFrom(notice)
            .where(
                likeTitle(noticeSearchCondition.getTitleLike()),
                likeContent(noticeSearchCondition.getContentLike())
            );

        return pagingUtil.getPageImpl(pageable, query, Notice.class);
    }

    private BooleanExpression likeTitle(String titleLike) {
        if (titleLike == null || titleLike.isEmpty()) {
            return null;
        }
        return notice.title.like("%" + titleLike + "%");
    }

    private BooleanExpression likeContent(String contentLike) {
        if (contentLike == null || contentLike.isEmpty()) {
            return null;
        }
        return notice.content.like("%" + contentLike + "%");
    }
}