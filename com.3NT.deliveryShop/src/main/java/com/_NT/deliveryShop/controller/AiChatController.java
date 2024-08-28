package com._NT.deliveryShop.controller;

import com._NT.deliveryShop.security.UserDetailsImpl;
import com._NT.deliveryShop.service.AiChatService;
import com._NT.deliveryShop.domain.dto.AiChatDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

import static com._NT.deliveryShop.domain.entity.UserRoleEnum.PreAuthorizeRole.ADMIN;
import static com._NT.deliveryShop.domain.entity.UserRoleEnum.PreAuthorizeRole.OWNER;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/ai")
public class AiChatController {

    private final AiChatService aiChatService;

    @PreAuthorize("hasAnyRole(" + ADMIN + "," + OWNER + ")")
    @PostMapping
    public AiChatDto.Response createChat(@RequestBody AiChatDto.Create aiChatDto,
                                         @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return aiChatService.createChat(aiChatDto, userDetails.getUser());
    }

    @PreAuthorize("hasAnyRole(" + ADMIN + "," + OWNER + ")")
    @GetMapping
    @ResponseBody
    public Collection<AiChatDto.GetAllChatsResponse> getAllChats(
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam("sort") String sort,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return aiChatService.getAllChats(page - 1, size, sort, userDetails.getUser());
    }

}
