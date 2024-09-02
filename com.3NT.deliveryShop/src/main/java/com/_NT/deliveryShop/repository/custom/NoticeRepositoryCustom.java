package com._NT.deliveryShop.repository.custom;

import com._NT.deliveryShop.domain.entity.Notice;
import com._NT.deliveryShop.repository.searchcondition.NoticeSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NoticeRepositoryCustom {

    Page<Notice> search(NoticeSearchCondition noticeSearchCondition, Pageable pageable);
}

