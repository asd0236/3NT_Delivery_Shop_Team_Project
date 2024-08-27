package com._NT.deliveryShop.controller;

import com._NT.deliveryShop.domain.dto.*;
import com._NT.deliveryShop.security.UserDetailsImpl;
import com._NT.deliveryShop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
    public OrderCreateResponseDto createOrder(@RequestBody OrderCreateRequestDto orderRequestDto,
                                              @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return orderService.createOrder(orderRequestDto, userDetails.getUser());
    }

    // 주문 단건 조회
    @GetMapping("/{orderId}")
    public OrderResponseDto getOrder(@PathVariable UUID orderId, UserDetailsImpl userDetails) {
        return orderService.getOrder(orderId, userDetails.getUser());
    }

    // 주문 삭제
    @DeleteMapping("/{orderId}")
    public OrderDeleteResponseDto deleteOrder(@PathVariable UUID orderId,
                                              @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return orderService.deleteOrder(orderId, userDetails.getUser());
    }

    // 주문 전체 조회
    @GetMapping
    public Page<OrderResponseDto> getOrders(
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam("sort") String sortBy,
            @RequestParam("isAsc") boolean isAsc,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return orderService.getAllOrders(page, size, sortBy, isAsc, userDetails.getUser());
    }

    // 주문 수정
    @PatchMapping("/{orderId}")
    public OrderResponseDto modifyOrder(@PathVariable UUID orderId,
                                        @RequestBody OrderModifyRequestDto orderModifyRequestDto,
                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return orderService.modifyOrder(orderId, orderModifyRequestDto, userDetails.getUser());
    }

}
