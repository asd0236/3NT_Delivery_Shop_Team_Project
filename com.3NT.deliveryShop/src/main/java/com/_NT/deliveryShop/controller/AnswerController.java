package com._NT.deliveryShop.controller;

import static com._NT.deliveryShop.domain.dto.AnswerDto.Create;
import static com._NT.deliveryShop.domain.dto.AnswerDto.Put;
import static com._NT.deliveryShop.domain.dto.AnswerDto.Result;
import static com._NT.deliveryShop.domain.entity.UserRoleEnum.PreAuthorizeRole.ADMIN;

import com._NT.deliveryShop.service.AnswerService;
import com._NT.deliveryShop.service.authorizer.AnswerAuthorizer;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "신고 게시글 답변", description = "신고 게시글 답변 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class AnswerController {

    private final AnswerService service;
    private final AnswerAuthorizer answerAuthorizer;

    @PreAuthorize("hasRole(" + ADMIN + ")")
    @PostMapping("/answers")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "신고 게시글 답변 등록", description = "신고 게시글 답변을 등록합니다.")
    @ApiResponse(responseCode = "201", description = "신고 게시글 답변 등록 성공")
    public Result postAnswer(@RequestBody Create dto, Authentication authentication) {

        answerAuthorizer.requireByOneself(authentication, dto.getOwnerId());
        return service.createAnswer(dto);
    }

    @GetMapping("/reports/{reportId}/answers")
    @Operation(summary = "신고 게시글을 기준으로 신고 게시글 답변 단건 조회", description = "신고 게시글 답변을 단건 조회합니다.")
    @ApiResponse(responseCode = "200", description = "신고 게시글 답변 조회 성공")

    public Result getAnswer(
        @Schema(description = "신고 게시글 식별자", example = "UUID")
        @PathVariable UUID reportId, Authentication authentication) {

        answerAuthorizer.requireReportOwner(authentication, reportId);
        return service.readAnswerByReportId(reportId);
    }

    @PreAuthorize("hasRole(" + ADMIN + ")")
    @GetMapping("/answers")
    @Operation(summary = "신고 게시글 답변 전체 조회", description = "신고 게시글 답변을 전체 조회합니다.")
    @ApiResponse(responseCode = "200", description = "신고 게시글 답변 전체 조회 성공")
    public List<Result> getAllAnswer(Pageable pageable) {

        return service.readAllAnswer(pageable);
    }

    @PreAuthorize("hasRole(" + ADMIN + ")")
    @PutMapping("/answers/{id}")
    @Operation(summary = "신고 게시글 답변 수정", description = "신고 게시글 답변을 수정합니다.")
    @ApiResponse(responseCode = "200", description = "신고 게시글 답변 수정 성공")
    public Result putAnswer(
        @Schema(description = "신고 게시글 답변 식별자", example = "UUID")
        @PathVariable UUID id,
        @RequestBody Put dto, Authentication authentication) {

        answerAuthorizer.requireByOneself(authentication, dto.getUpdater());
        return service.putAnswer(id, dto);
    }

    @PreAuthorize("hasRole(" + ADMIN + ")")
    @DeleteMapping("/answers/{id}")
    @Operation(summary = "신고 게시글 답변 삭제", description = "신고 게시글 답변을 소프트 삭제합니다.")
    @ApiResponse(responseCode = "200", description = "신고 게시글 답변 삭제 성공")
    public Result.Deleted deleteAnswer(
        @Schema(description = "신고 게시글 답변 식별자", example = "UUID")
        @PathVariable UUID id, Authentication authentication) {

        return service.deleteAnswer(id, authentication);
    }
}