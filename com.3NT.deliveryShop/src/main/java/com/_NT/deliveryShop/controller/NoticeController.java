package com._NT.deliveryShop.controller;

import static com._NT.deliveryShop.domain.dto.NoticeDto.Create;
import static com._NT.deliveryShop.domain.dto.NoticeDto.Put;
import static com._NT.deliveryShop.domain.dto.NoticeDto.Result;
import static com._NT.deliveryShop.domain.entity.UserRoleEnum.PreAuthorizeRole.ADMIN;

import com._NT.deliveryShop.repository.searchcondition.NoticeSearchCondition;
import com._NT.deliveryShop.service.NoticeService;
import com._NT.deliveryShop.service.authorizer.NoticeAuthorizer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "공지사항", description = "공지사항 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notices")
public class NoticeController {

    private final NoticeService service;
    private final NoticeAuthorizer noticeAuthorizer;


    @PreAuthorize("hasRole(" + ADMIN + ")")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "공지사항 등록", description = "공지사항을 등록합니다.")
    @ApiResponse(responseCode = "201", description = "공지사항 등록 성공")
    public Result postNotice(@RequestBody Create dto, Authentication authentication) {

        noticeAuthorizer.requireByOneself(authentication, dto.getOwnerId());
        return service.createNotice(dto);
    }

    @GetMapping("/{id}")
    @Operation(summary = "공지사항 단건 조회", description = "공지사항을 단건 조회합니다.")
    @ApiResponse(responseCode = "200", description = "공지사항 조회 성공")
    public Result getNotice(
        @Schema(description = "공지사항 식별자", example = "UUID")
        @PathVariable UUID id) {
        return service.readNotice(id);
    }

    @GetMapping
    @Operation(summary = "공지사항 전체 조회", description = "공지사항을 전체 조회합니다.")
    @ApiResponse(responseCode = "200", description = "공지사항 전체 조회 성공")
    public List<Result> getAllNotice(Pageable pageable) {

        return service.readAllNotice(pageable);
    }

    @GetMapping("/search")
    @Operation(summary = "공지사항 검색", description = "공지사항을 검색합니다.")
    @ApiResponse(responseCode = "200", description = "공지사항 검색 성공")
    public List<Result> searchNotice(
        @Schema(description = "검색에 포함 시키고 싶은 공지사항 제목", example = "title")
        @RequestParam(required = false) String titleLike,
        @Schema(description = "검색에 포함 시키고 싶은 공지사항 내용", example = "title")
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
    @Operation(summary = "공지사항 수정", description = "공지사항을 수정합니다.")
    @ApiResponse(responseCode = "200", description = "공지사항 수정 성공")
    public Result putNotice(
        @Schema(description = "공지사항 식별자", example = "UUID")
        @PathVariable UUID id,
        @RequestBody Put dto, Authentication authentication) {

        noticeAuthorizer.requireByOneself(authentication, dto.getUpdater());
        return service.putNotice(id, dto);
    }

    @PreAuthorize("hasRole(" + ADMIN + ")")
    @DeleteMapping("/{id}")
    @Operation(summary = "공지사항 삭제", description = "공지사항을 소프트 삭제합니다.")
    @ApiResponse(responseCode = "200", description = "공지사항 삭제 성공")
    public Result.Deleted deleteNotice(
        @Schema(description = "공지사항 식별자", example = "UUID")
        @PathVariable UUID id, Authentication authentication) {

        return service.deleteNotice(id, authentication);
    }
}