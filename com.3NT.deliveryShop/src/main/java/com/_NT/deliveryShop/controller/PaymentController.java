package com._NT.deliveryShop.controller;

import com._NT.deliveryShop.domain.dto.PaymentDto;
import com._NT.deliveryShop.security.UserDetailsImpl;
import com._NT.deliveryShop.service.PaymentService;
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
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    @ResponseBody
    public PaymentDto.Response createPayment(@RequestBody PaymentDto.Create paymentDto,
                                             @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return paymentService.createPayment(paymentDto, userDetails.getUser());
    }


    @GetMapping("/{paymentId}")
    @ResponseBody
    public PaymentDto.Response getPayment(@PathVariable UUID paymentId,
                                          @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return paymentService.getPayment(paymentId, userDetails.getUser());
    }

    @GetMapping
    @ResponseBody
    public Collection<PaymentDto.GetAllPaymentsResponse> getAllPayments(
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam("sort") String sort,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return paymentService.getAllPayments(page - 1, size, sort, userDetails.getUser());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{paymentId}")
    @ResponseBody
    public PaymentDto.Response modifyPayment(@PathVariable UUID paymentId,
                                             @RequestBody PaymentDto.Modify paymentDto) {
        return paymentService.modifyPayment(paymentId, paymentDto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{paymentId}")
    @ResponseBody
    public PaymentDto.DeletePaymentResult deletePayment(@PathVariable UUID paymentId,
                                                        @AuthenticationPrincipal UserDetailsImpl userDetails){
        return paymentService.deletePayment(paymentId, userDetails.getUser());
    }

}
