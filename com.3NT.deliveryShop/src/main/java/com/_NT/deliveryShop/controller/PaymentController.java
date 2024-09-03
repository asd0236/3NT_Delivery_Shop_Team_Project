package com._NT.deliveryShop.controller;

import static com._NT.deliveryShop.common.codes.SuccessCode.DELETE_SUCCESS;
import static com._NT.deliveryShop.common.codes.SuccessCode.INSERT_SUCCESS;
import static com._NT.deliveryShop.common.codes.SuccessCode.SELECT_SUCCESS;
import static com._NT.deliveryShop.domain.dto.PaymentDto.Create;
import static com._NT.deliveryShop.domain.dto.PaymentDto.DeletePaymentResult;
import static com._NT.deliveryShop.domain.dto.PaymentDto.GetAllPaymentsResponse;
import static com._NT.deliveryShop.domain.dto.PaymentDto.Response;

import com._NT.deliveryShop.common.response.ResultResponse;
import com._NT.deliveryShop.security.UserDetailsImpl;
import com._NT.deliveryShop.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Collection;
import java.util.UUID;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

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
    @ApiResponse(responseCode = "201", description = "결제 생성 성공")
    @ResponseStatus(HttpStatus.CREATED)
    public ResultResponse<Response> createPayment(@RequestBody @Valid Create paymentDto,
                                             @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResultResponse.<Response>successBuilder()
            .result(paymentService.createPayment(paymentDto, userDetails.getUser()))
            .successCode(INSERT_SUCCESS)
            .build();
    }


    @GetMapping("/{paymentId}")
    @ResponseBody
    @Operation(summary = "결제 단건 조회", description = "결제를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "결제 조회 성공")
    public ResultResponse<Response> getPayment(
            @Schema(description = "결제 식별자", example = "UUID", required = true)
            @PathVariable UUID paymentId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResultResponse.<Response>successBuilder()
            .result(paymentService.getPayment(paymentId, userDetails.getUser()))
            .successCode(SELECT_SUCCESS)
            .build();
    }

    @GetMapping
    @ResponseBody
    @Operation(summary = "결제 페이징 조회", description = "결제를 페이징 조회합니다.")
    @ApiResponse(responseCode = "200", description = "결제 페이징 조회 성공")
    public ResultResponse<Collection<GetAllPaymentsResponse>> getAllPayments(
            @Schema(description = "페이지 번호(1부터 N까지)", defaultValue = "1")
            @RequestParam("page") int page,
            @Schema(description = "페이지에 출력할 개수를 입력합니다.", defaultValue = "10")
            @RequestParam("size") int size,
            @Schema(description = "정렬 기준을 입력합니다.")
            @RequestParam("sort") String sort,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResultResponse.<Collection<GetAllPaymentsResponse>>successBuilder()
            .result(paymentService.getAllPayments(page - 1, size, sort, userDetails.getUser()))
            .successCode(SELECT_SUCCESS)
            .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{paymentId}")
    @ResponseBody
    @Operation(summary = "결제 삭제", description = "결제를 소프트 삭제합니다.")
    @ApiResponse(responseCode = "200", description = "결제 삭제 성공")
    public ResultResponse<DeletePaymentResult> deletePayment(
            @Schema(description = "결제 식별자", example = "UUID", required = true)
            @PathVariable UUID paymentId,
            @AuthenticationPrincipal UserDetailsImpl userDetails){

        return ResultResponse.<DeletePaymentResult>successBuilder()
            .result(paymentService.deletePayment(paymentId, userDetails.getUser()))
            .successCode(DELETE_SUCCESS)
            .build();
    }

}
