package com._NT.deliveryShop.controller;

import com._NT.deliveryShop.domain.dto.OrderCreateRequestDto;
import com._NT.deliveryShop.domain.dto.OrderCreateResponseDto;
import com._NT.deliveryShop.domain.dto.OrderDeleteResponseDto;
import com._NT.deliveryShop.domain.dto.OrderResponseDto;
import com._NT.deliveryShop.domain.entity.User;
import com._NT.deliveryShop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController {
    private final OrderService orderService;

    // 주문 등록
    @PostMapping
    public OrderCreateResponseDto createOrder(@RequestBody OrderCreateRequestDto orderRequestDto, @AuthenticationPrincipal User user) {
        return orderService.createOrder(orderRequestDto, user);
    }

    // 주문 단건 조회
    @GetMapping("/{orderId}")
    public OrderResponseDto getOrder(@PathVariable UUID orderId, @AuthenticationPrincipal User user) {
        return orderService.getOrder(orderId, user);
    }

}
