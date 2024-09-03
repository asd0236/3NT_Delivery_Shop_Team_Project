package com._NT.deliveryShop.controller;

import static com._NT.deliveryShop.common.codes.SuccessCode.INSERT_SUCCESS;
import static com._NT.deliveryShop.common.codes.SuccessCode.SELECT_SUCCESS;
import static com._NT.deliveryShop.domain.dto.AiChatDto.Create;
import static com._NT.deliveryShop.domain.dto.AiChatDto.GetAllChatsResponse;
import static com._NT.deliveryShop.domain.dto.AiChatDto.Response;
import static com._NT.deliveryShop.domain.entity.UserRoleEnum.PreAuthorizeRole.ADMIN;
import static com._NT.deliveryShop.domain.entity.UserRoleEnum.PreAuthorizeRole.OWNER;

import com._NT.deliveryShop.common.response.ResultResponse;
import com._NT.deliveryShop.security.UserDetailsImpl;
import com._NT.deliveryShop.service.AiChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Collection;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "AI", description = "AI 채팅 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/ai")
public class AiChatController {

    private final AiChatService aiChatService;

    @PreAuthorize("hasAnyRole(" + ADMIN + "," + OWNER + ")")
    @PostMapping
    @Operation(summary = "AI 채팅 생성", description = "AI 채팅을 생성합니다.")
    @ApiResponse(responseCode = "201", description = "AI 채팅 생성 성공")
    @ResponseStatus(HttpStatus.CREATED)
    public ResultResponse<Response> createChat(@RequestBody @Valid Create aiChatDto,
                                         @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResultResponse.<Response>successBuilder()
            .result(aiChatService.createChat(aiChatDto, userDetails.getUser()))
            .successCode(INSERT_SUCCESS)
            .build();
    }

    @PreAuthorize("hasAnyRole(" + ADMIN + "," + OWNER + ")")
    @GetMapping
    @Operation(summary = "AI 채팅 페이징 조회", description = "AI 채팅을 페이징 조회합니다.")
    @ApiResponse(responseCode = "200", description = "AI 채팅 페이징 조회 성공")
    public ResultResponse<Collection<GetAllChatsResponse>> getAllChats(
            @Schema(description = "페이지 번호(1부터 N까지)", defaultValue = "1")
            @RequestParam("page") int page,
            @Schema(description = "페이지에 출력할 개수를 입력합니다.", defaultValue = "10")
            @RequestParam("size") int size,
            @Schema(description = "정렬 기준을 입력합니다.")
            @RequestParam("sort") String sort,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResultResponse.<Collection<GetAllChatsResponse>>successBuilder()
            .result(aiChatService.getAllChats(page - 1, size, sort, userDetails.getUser()))
            .successCode(SELECT_SUCCESS)
            .build();
    }

}
