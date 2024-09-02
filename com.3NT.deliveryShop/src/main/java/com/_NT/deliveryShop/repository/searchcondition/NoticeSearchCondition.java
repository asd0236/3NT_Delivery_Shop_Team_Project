package com._NT.deliveryShop.repository.searchcondition;

import lombok.Builder;
import lombok.Getter;

@Getter
public final class NoticeSearchCondition {

    private final String titleLike;
    private final String contentLike;


    @Builder
    public NoticeSearchCondition(String titleLike, String contentLike) {
        this.titleLike = titleLike;
        this.contentLike = contentLike;
    }
}
