package com._NT.deliveryShop.controller;

import com._NT.deliveryShop.security.UserDetailsImpl;
import com._NT.deliveryShop.service.AiChatService;
import com._NT.deliveryShop.domain.dto.AiChatDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

import static com._NT.deliveryShop.domain.entity.UserRoleEnum.PreAuthorizeRole.ADMIN;
import static com._NT.deliveryShop.domain.entity.UserRoleEnum.PreAuthorizeRole.OWNER;

@Tag(name = "AI", description = "AI 채팅 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/ai")
public class AiChatController {

    private final AiChatService aiChatService;

    @PreAuthorize("hasAnyRole(" + ADMIN + "," + OWNER + ")")
    @PostMapping
    @Operation(summary = "AI 채팅 생성", description = "AI 채팅을 생성합니다.")
    @ApiResponse(responseCode = "200", description = "AI 채팅 생성 성공")
    public AiChatDto.Response createChat(@RequestBody AiChatDto.Create aiChatDto,
                                         @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return aiChatService.createChat(aiChatDto, userDetails.getUser());
    }

    @PreAuthorize("hasAnyRole(" + ADMIN + "," + OWNER + ")")
    @GetMapping
    @Operation(summary = "AI 채팅 페이징 조회", description = "AI 채팅을 페이징 조회합니다.")
    @ApiResponse(responseCode = "200", description = "AI 채팅 페이징 조회 성공")
    public Collection<AiChatDto.GetAllChatsResponse> getAllChats(
            @Schema(description = "페이지 번호(1부터 N까지)", defaultValue = "1")
            @RequestParam("page") int page,
            @Schema(description = "페이지에 출력할 개수를 입력합니다.", defaultValue = "10")
            @RequestParam("size") int size,
            @Schema(description = "정렬 기준을 입력합니다.")
            @RequestParam("sort") String sort,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return aiChatService.getAllChats(page - 1, size, sort, userDetails.getUser());
    }

}
