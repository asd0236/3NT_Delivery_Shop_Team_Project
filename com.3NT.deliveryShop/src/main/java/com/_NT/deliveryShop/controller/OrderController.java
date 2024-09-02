package com._NT.deliveryShop.controller;

import static com._NT.deliveryShop.domain.dto.OrderDto.CreateOrderRequest;
import static com._NT.deliveryShop.domain.dto.OrderDto.CreateOrderResponse;
import static com._NT.deliveryShop.domain.dto.OrderDto.DeleteResponse;
import static com._NT.deliveryShop.domain.dto.OrderDto.ModifyRequest;
import static com._NT.deliveryShop.domain.dto.OrderDto.OrderResponse;
import static com._NT.deliveryShop.domain.entity.UserRoleEnum.PreAuthorizeRole.ADMIN;
import static com._NT.deliveryShop.domain.entity.UserRoleEnum.PreAuthorizeRole.USER;

import com._NT.deliveryShop.security.UserDetailsImpl;
import com._NT.deliveryShop.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "주문", description = "주문 등록, 조회, 수정, 삭제 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController {
    private final OrderService orderService;

    // 주문 등록
    @PostMapping
    @PreAuthorize("hasAnyRole(" + ADMIN +  "," + USER + ")")
    @Operation(summary = "주문 등록", description = "주문을 등록합니다.")
    @ApiResponse(responseCode = "201", description = "주문 등록 성공")
    @ResponseStatus(HttpStatus.CREATED)
    public CreateOrderResponse createOrder(@RequestBody CreateOrderRequest orderRequestDto,
                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return orderService.createOrder(orderRequestDto, userDetails.getUser());
    }

    // 주문 단건 조회
    @GetMapping("/{orderId}")
    @Operation(summary = "주문 단건 조회", description = "주문을 단건 조회합니다.")
    @ApiResponse(responseCode = "200", description = "주문 조회 성공")
    public OrderResponse getOrder(
            @Parameter(description = "주문 식별자", example = "UUID", required = true)
            @PathVariable UUID orderId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return orderService.getOrder(orderId, userDetails.getUser());
    }

    // 주문 삭제
    @DeleteMapping("/{orderId}")
    @Operation(summary = "주문 삭제", description = "주문을 소프트 삭제합니다.")
    @ApiResponse(responseCode = "200", description = "주문 삭제 성공")
    public DeleteResponse deleteOrder(
            @Parameter(description = "주문 식별자", example = "UUID", required = true)
            @PathVariable UUID orderId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return orderService.deleteOrder(orderId, userDetails.getUser());
    }

    // 주문 전체 조회
    @GetMapping
    @Operation(summary = "주문 전체 조회", description = "주문을 전체 조회합니다.")
    @ApiResponse(responseCode = "200", description = "주문 전체 조회 성공")
    public Page<OrderResponse> getOrders(
            @Schema(description = "페이지 번호(0부터 N까지)", defaultValue = "0")
            @RequestParam("page") int page,
            @Schema(description = "페이지에 출력할 개수를 입력합니다.", defaultValue = "10")
            @RequestParam("size") int size,
            @Schema(description = "정렬 기준을 입력합니다.", defaultValue = "createdDate")
            @RequestParam("sort") String sortBy,
            @Schema(description = "정렬 방식을 입력합니다.", defaultValue = "true")
            @RequestParam("isAsc") boolean isAsc,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return orderService.getAllOrders(page, size, sortBy, isAsc, userDetails.getUser());
    }

    // 주문 수정
    @PatchMapping("/{orderId}")
    @Operation(summary = "주문 수정", description = "주문을 수정합니다.")
    @ApiResponse(responseCode = "200", description = "주문 수정 성공")
    public OrderResponse modifyOrder(
            @Parameter(description = "주문 식별자", example = "UUID", required = true)
            @PathVariable UUID orderId,
            @RequestBody ModifyRequest orderModifyRequestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return orderService.modifyOrder(orderId, orderModifyRequestDto, userDetails.getUser());
    }

}
