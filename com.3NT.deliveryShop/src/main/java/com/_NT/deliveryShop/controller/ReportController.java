package com._NT.deliveryShop.controller;

import static com._NT.deliveryShop.common.codes.SuccessCode.DELETE_SUCCESS;
import static com._NT.deliveryShop.common.codes.SuccessCode.INSERT_SUCCESS;
import static com._NT.deliveryShop.common.codes.SuccessCode.SELECT_SUCCESS;
import static com._NT.deliveryShop.common.codes.SuccessCode.UPDATE_SUCCESS;
import static com._NT.deliveryShop.domain.dto.ReportDto.Create;
import static com._NT.deliveryShop.domain.dto.ReportDto.Put;
import static com._NT.deliveryShop.domain.dto.ReportDto.Result;

import com._NT.deliveryShop.common.response.ResultResponse;
import com._NT.deliveryShop.domain.entity.UserRoleEnum;
import com._NT.deliveryShop.security.UserDetailsImpl;
import com._NT.deliveryShop.service.ReportService;
import com._NT.deliveryShop.service.authorizer.ReportAuthorizer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "신고 게시글", description = "신고 게시글 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reports")
public class ReportController {

    private final ReportService service;
    private final ReportAuthorizer reportAuthorizer;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "신고 게시글 등록", description = "신고 게시글을 등록합니다.")
    @ApiResponse(responseCode = "201", description = "신고 게시글 등록 성공")
    public ResultResponse<Result> postReport(@RequestBody @Valid Create dto,
        Authentication authentication) {

        reportAuthorizer.requireByOneself(authentication, dto.getOwnerId());
        return ResultResponse.<Result>successBuilder()
            .result(service.createReport(dto))
            .successCode(INSERT_SUCCESS)
            .build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "신고 게시글 단건 조회", description = "신고 게시글을 단건 조회합니다.")
    @ApiResponse(responseCode = "200", description = "신고 게시글 조회 성공")
    public ResultResponse<Result> getReport(
        @Schema(description = "신고 게시글 식별자", example = "UUID")
        @PathVariable UUID id, Authentication authentication) {

        reportAuthorizer.requireReportOwner(authentication, id);

        return ResultResponse.<Result>successBuilder()
            .result(service.readReport(id))
            .successCode(SELECT_SUCCESS)
            .build();
    }

    @GetMapping
    @Operation(summary = "신고 게시글 전체 조회",
        description = "신고 게시글을 전체 조회합니다.\n"
            + "일반 사용자의 경우 본인이 작성한 신고 게시글을 기준으로 검색합니다\n"
            + "관리자의 경우 모든 신고 게시글을 기준으로 검색합니다")
    @ApiResponse(responseCode = "200", description = "신고 게시글 전체 조회 성공")
    public ResultResponse<List<Result>> getAllReport(Pageable pageable,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {

        UserRoleEnum role = userDetails.getUser().getRole();
        List<Result> results;
        if (role.equals(UserRoleEnum.ADMIN)) {

            results = service.readAllReport(pageable);
        } else {
            results = service.readAllReportByUserId(userDetails.getUser().getUserId(), pageable);
        }

        return ResultResponse.<List<Result>>successBuilder()
            .result(results)
            .successCode(SELECT_SUCCESS)
            .build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "신고 게시글 수정", description = "신고 게시글을 수정합니다.")
    @ApiResponse(responseCode = "200", description = "신고 게시글 수정 성공")
    public ResultResponse<Result> putReport(
        @Schema(description = "신고 게시글 식별자", example = "UUID")
        @PathVariable UUID id,
        @RequestBody @Valid Put dto, Authentication authentication) {

        reportAuthorizer.requireReportOwner(authentication, id);
        return ResultResponse.<Result>successBuilder()
            .result(service.putReport(id, dto))
            .successCode(UPDATE_SUCCESS)
            .build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "신고 게시글 삭제", description = "신고 게시글을 소프트 삭제합니다.")
    @ApiResponse(responseCode = "200", description = "신고 게시글 삭제 성공")
    public ResultResponse<Result.Deleted> deleteReport(
        @Schema(description = "신고 게시글 식별자", example = "UUID")
        @PathVariable UUID id, Authentication authentication) {

        reportAuthorizer.requireReportOwner(authentication, id);
        return ResultResponse.<Result.Deleted>successBuilder()
            .result(service.deleteReport(id, authentication))
            .successCode(DELETE_SUCCESS)
            .build();
    }
}