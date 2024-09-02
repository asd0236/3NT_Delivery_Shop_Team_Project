package com._NT.deliveryShop.controller;

import com._NT.deliveryShop.common.codes.SuccessCode;
import com._NT.deliveryShop.common.response.ResultResponse;
import com._NT.deliveryShop.security.UserDetailsImpl;
import com._NT.deliveryShop.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com._NT.deliveryShop.domain.dto.OrderDto.*;
import static com._NT.deliveryShop.domain.entity.UserRoleEnum.PreAuthorizeRole.*;

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
    public ResultResponse<CreateOrderResponse> createOrder(@RequestBody @Valid CreateOrderRequest orderRequestDto,
                                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        CreateOrderResponse order = orderService.createOrder(orderRequestDto, userDetails.getUser());

        return ResultResponse.<CreateOrderResponse>builder()
                .result(order)
                .resultCode(SuccessCode.INSERT_SUCCESS.getStatus())
                .resultMessage(SuccessCode.INSERT_SUCCESS.getMessage())
                .build();
    }

    // 주문 단건 조회
    @GetMapping("/{orderId}")
    @Operation(summary = "주문 단건 조회", description = "주문을 단건 조회합니다.")
    @ApiResponse(responseCode = "200", description = "주문 조회 성공")
    public ResultResponse<OrderResponse> getOrder(
            @Parameter(description = "주문 식별자", example = "UUID", required = true)
            @PathVariable UUID orderId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        OrderResponse order = orderService.getOrder(orderId, userDetails.getUser());

        return ResultResponse.<OrderResponse>builder()
                .result(order)
                .resultCode(SuccessCode.SELECT_SUCCESS.getStatus())
                .resultMessage(SuccessCode.SELECT_SUCCESS.getMessage())
                .build();
    }

    // 주문 삭제
    @DeleteMapping("/{orderId}")
    @Operation(summary = "주문 삭제", description = "주문을 소프트 삭제합니다.")
    @ApiResponse(responseCode = "200", description = "주문 삭제 성공")
    public ResultResponse<DeleteResponse> deleteOrder(
            @Parameter(description = "주문 식별자", example = "UUID", required = true)
            @PathVariable UUID orderId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        DeleteResponse deleteResponse = orderService.deleteOrder(orderId, userDetails.getUser());

        return ResultResponse.<DeleteResponse>builder()
                .result(deleteResponse)
                .resultCode(SuccessCode.DELETE_SUCCESS.getStatus())
                .resultMessage(SuccessCode.DELETE_SUCCESS.getMessage())
                .build();
    }

    // 주문 전체 조회
    @GetMapping
    @Operation(summary = "주문 전체 조회", description = "주문을 전체 조회합니다.")
    @ApiResponse(responseCode = "200", description = "주문 전체 조회 성공")
    public ResultResponse<Page<OrderResponse>> getOrders(
            @Schema(description = "페이지 번호(0부터 N까지)", defaultValue = "0")
            @RequestParam("page") int page,
            @Schema(description = "페이지에 출력할 개수를 입력합니다.", defaultValue = "10")
            @RequestParam("size") int size,
            @Schema(description = "정렬 기준을 입력합니다.", defaultValue = "createdDate")
            @RequestParam("sort") String sortBy,
            @Schema(description = "정렬 방식을 입력합니다.", defaultValue = "true")
            @RequestParam("isAsc") boolean isAsc,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Page<OrderResponse> allOrders = orderService.getAllOrders(page, size, sortBy, isAsc, userDetails.getUser());

        return ResultResponse.<Page<OrderResponse>>builder()
                .result(allOrders)
                .resultCode(SuccessCode.SELECT_SUCCESS.getStatus())
                .resultMessage(SuccessCode.SELECT_SUCCESS.getMessage())
                .build();
    }

    // 주문 수정
    @PatchMapping("/{orderId}")
    @Operation(summary = "주문 수정", description = "주문을 수정합니다.")
    @ApiResponse(responseCode = "200", description = "주문 수정 성공")
    public ResultResponse<OrderResponse> modifyOrder(
            @Parameter(description = "주문 식별자", example = "UUID", required = true)
            @PathVariable UUID orderId,
            @RequestBody @Valid ModifyRequest orderModifyRequestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        OrderResponse orderResponse = orderService.modifyOrder(orderId, orderModifyRequestDto, userDetails.getUser());
        return ResultResponse.<OrderResponse>builder()
                .result(orderResponse)
                .resultCode(SuccessCode.UPDATE_SUCCESS.getStatus())
                .resultMessage(SuccessCode.UPDATE_SUCCESS.getMessage())
                .build();
    }

}
