package com._NT.deliveryShop.controller;

import com._NT.deliveryShop.domain.dto.PaymentDto;
import com._NT.deliveryShop.security.UserDetailsImpl;
import com._NT.deliveryShop.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.UUID;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/payments")
@Tag(name = "결제", description = "결제 생성, 조회, 삭제 API")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    @ResponseBody
    @Operation(summary = "결제 생성", description = "결제를 생성합니다.")
    @ApiResponse(responseCode = "200", description = "결제 생성 성공")
    public PaymentDto.Response createPayment(@RequestBody PaymentDto.Create paymentDto,
                                             @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return paymentService.createPayment(paymentDto, userDetails.getUser());
    }


    @GetMapping("/{paymentId}")
    @ResponseBody
    @Operation(summary = "결제 단건 조회", description = "결제를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "결제 조회 성공")
    public PaymentDto.Response getPayment(
            @Schema(description = "결제 식별자", example = "UUID", required = true)
            @PathVariable UUID paymentId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return paymentService.getPayment(paymentId, userDetails.getUser());
    }

    @GetMapping
    @ResponseBody
    @Operation(summary = "결제 페이징 조회", description = "결제를 페이징 조회합니다.")
    @ApiResponse(responseCode = "200", description = "결제 페이징 조회 성공")
    public Collection<PaymentDto.GetAllPaymentsResponse> getAllPayments(
            @Schema(description = "페이지 번호(1부터 N까지)", defaultValue = "1")
            @RequestParam("page") int page,
            @Schema(description = "페이지에 출력할 개수를 입력합니다.", defaultValue = "10")
            @RequestParam("size") int size,
            @Schema(description = "정렬 기준을 입력합니다.")
            @RequestParam("sort") String sort,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return paymentService.getAllPayments(page - 1, size, sort, userDetails.getUser());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{paymentId}")
    @ResponseBody
    @Operation(summary = "결제 삭제", description = "결제를 소프트 삭제합니다.")
    @ApiResponse(responseCode = "200", description = "결제 삭제 성공")
    public PaymentDto.DeletePaymentResult deletePayment(
            @Schema(description = "결제 식별자", example = "UUID", required = true)
            @PathVariable UUID paymentId,
            @AuthenticationPrincipal UserDetailsImpl userDetails){
        return paymentService.deletePayment(paymentId, userDetails.getUser());
    }

}
