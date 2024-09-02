package com._NT.deliveryShop.controller;

import static com._NT.deliveryShop.domain.dto.NoticeDto.Create;
import static com._NT.deliveryShop.domain.dto.NoticeDto.Put;
import static com._NT.deliveryShop.domain.dto.NoticeDto.Result;
import static com._NT.deliveryShop.domain.entity.UserRoleEnum.PreAuthorizeRole.ADMIN;

import com._NT.deliveryShop.repository.searchcondition.NoticeSearchCondition;
import com._NT.deliveryShop.service.NoticeService;
import com._NT.deliveryShop.service.authorizer.NoticeAuthorizer;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notices")
public class NoticeController {

    private final NoticeService service;
    private final NoticeAuthorizer noticeAuthorizer;


    @PreAuthorize("hasRole(" + ADMIN + ")")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Result postNotice(@RequestBody Create dto, Authentication authentication) {

        noticeAuthorizer.requireByOneself(authentication, dto.getOwnerId());
        return service.createNotice(dto);
    }

    @GetMapping("/{id}")
    public Result getNotice(@PathVariable UUID id) {
        return service.readNotice(id);
    }

    @GetMapping
    public List<Result> getAllNotice(Pageable pageable) {

        return service.readAllNotice(pageable);
    }

    @GetMapping("/search")
    public List<Result> searchNotice(
        @RequestParam(required = false) String titleLike,
        @RequestParam(required = false) String contentLike,
        Pageable pageable) {
        NoticeSearchCondition noticeSearchCondition = NoticeSearchCondition.builder()
            .titleLike(titleLike)
            .contentLike(contentLike)
            .build();

        return service.searchNotice(noticeSearchCondition, pageable);
    }

    @PreAuthorize("hasRole(" + ADMIN + ")")
    @PutMapping("/{id}")
    public Result putNotice(@PathVariable UUID id,
        @RequestBody Put dto, Authentication authentication) {

        noticeAuthorizer.requireByOneself(authentication, dto.getUpdater());
        return service.putNotice(id, dto);
    }

    @PreAuthorize("hasRole(" + ADMIN + ")")
    @DeleteMapping("/{id}")
    public Result.Deleted deleteNotice(@PathVariable UUID id, Authentication authentication) {

        return service.deleteNotice(id, authentication);
    }
}