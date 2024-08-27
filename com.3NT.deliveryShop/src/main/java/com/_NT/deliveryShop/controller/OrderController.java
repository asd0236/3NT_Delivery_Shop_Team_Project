package com._NT.deliveryShop.controller;

import com._NT.deliveryShop.domain.dto.OrderCreateRequestDto;
import com._NT.deliveryShop.domain.dto.OrderCreateResponseDto;
import com._NT.deliveryShop.domain.dto.OrderDeleteResponseDto;
import com._NT.deliveryShop.domain.dto.OrderResponseDto;
import com._NT.deliveryShop.security.UserDetailsImpl;
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
    public OrderCreateResponseDto createOrder(@RequestBody OrderCreateRequestDto orderRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return orderService.createOrder(orderRequestDto, userDetails.getUser());
    }

    // 주문 단건 조회
    @GetMapping("/{orderId}")
    public OrderResponseDto getOrder(@PathVariable UUID orderId, UserDetailsImpl userDetails) {
        return orderService.getOrder(orderId, userDetails.getUser());
    }

    // 주문 삭제
    @DeleteMapping("/{orderId}")
    public OrderDeleteResponseDto deleteOrder(@PathVariable UUID orderId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return orderService.deleteOrder(orderId, userDetails.getUser());
    }

}
