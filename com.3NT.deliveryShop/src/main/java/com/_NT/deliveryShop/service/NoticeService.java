package com._NT.deliveryShop.service;

import static com._NT.deliveryShop.domain.dto.NoticeDto.Create;
import static com._NT.deliveryShop.domain.dto.NoticeDto.Put;
import static com._NT.deliveryShop.domain.dto.NoticeDto.Result;

import com._NT.deliveryShop.domain.entity.Notice;
import com._NT.deliveryShop.domain.entity.User;
import com._NT.deliveryShop.repository.NoticeRepository;
import com._NT.deliveryShop.repository.helper.RepositoryHelper;
import com._NT.deliveryShop.repository.implementaion.NoticeRepositoryImpl;
import com._NT.deliveryShop.repository.searchcondition.NoticeSearchCondition;
import com._NT.deliveryShop.service.authorizer.AuthenticationInspector;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final RepositoryHelper repoHelper;
    private final AuthenticationInspector inspector;
    private final NoticeRepositoryImpl noticeRepositoryImpl;

    @Transactional
    public Result createNotice(Create dto) {
        User owner = repoHelper.findUserOrThrow404(dto.getOwnerId());
        Notice notice = dto.asEntity(it -> it
            .withOwner(owner));

        return Result.of(noticeRepository.save(notice));
    }

    public Result readNotice(UUID id) {
        return Result.of(repoHelper.findNoticeOrThrow404(id));
    }

    public List<Result> readAllNotice(Pageable pageable) {
        return Result.of(noticeRepository.findAllByIsDeletedFalse(pageable));
    }

    public List<Result> searchNotice(NoticeSearchCondition condition, Pageable pageable) {
        return Result.of(noticeRepositoryImpl.search(condition, pageable));
    }

    @Transactional
    public Result putNotice(UUID id, Put dto) {
        Notice notice = repoHelper.findNoticeOrThrow404(id);

        notice.setTitle(dto.getTitle());
        notice.setContent(dto.getContent());
        notice.setUpdateBy(dto.getUpdater());

        return Result.of(noticeRepository.save(notice));
    }

    @Transactional
    public Result.Deleted deleteNotice(UUID id, Authentication authentication) {
        Notice notice = repoHelper.findNoticeOrThrow404(id);
        Long deleter = inspector.getUserOrThrow(authentication).getUserId();

        noticeRepository.softDeleteNotice(id, LocalDateTime.now(), deleter);
        return Result.Deleted.of(notice);
    }
}
