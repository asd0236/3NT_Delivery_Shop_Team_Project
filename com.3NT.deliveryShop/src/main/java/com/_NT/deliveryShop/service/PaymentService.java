package com._NT.deliveryShop.service;

import com._NT.deliveryShop.domain.dto.PaymentDto;
import com._NT.deliveryShop.domain.entity.*;
import com._NT.deliveryShop.repository.OrderRepository;
import com._NT.deliveryShop.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Objects;
import java.util.UUID;

import static com._NT.deliveryShop.domain.entity.UserRoleEnum.ADMIN;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    @Transactional
    public PaymentDto.Response createPayment(PaymentDto.Create paymentDto, User user) {

        Order order = orderRepository.findById(paymentDto.getOrderId()).orElse(null);

        if (order == null || order.getIsDeleted()) {
            throw new IllegalArgumentException("존재하지 않는 주문입니다");
        }

        // 관리자가 아닐 경우 타인의 주문 접근 시도 시 403 에러 반환
        if (user.getRole() != ADMIN && !Objects.equals(user.getUserId(), order.getUser().getUserId())) {
            throw new AccessDeniedException("접근 권한이 없습니다.");
        }

        // 외부 API 요청 및 결제 완료를 가정
        try {
            // 결제 성공
            Payment newPayment = new Payment(order, paymentDto.getAmount(), LocalDateTime.now(), PaymentMethod.CREDIT_CARD);

            order.setPayment(newPayment);
            order.setStatus(OrderStatus.PAYMENT_COMPLETED);

            return PaymentDto.Response.of(paymentRepository.save(newPayment));
        } catch(Exception e){
            // 결제 실패 시 주문 취소

            order.setStatus(OrderStatus.PAYMENT_CANCELED);
            throw new RuntimeException("결제에 실패하여 주문이 취소되었습니다.", e);
        }
    }

    @Transactional(readOnly = true)
    public PaymentDto.Response getPayment(UUID paymentId, User user) {

        Payment payment = paymentRepository.findById(paymentId).orElse(null);

        if(payment == null || payment.getIsDeleted()) {
            throw new IllegalArgumentException("존재하지 않는 결제 정보입니다");
        }

        // 관리자가 아닐 경우 타인의 결제 내역 조회 시도 시 403 에러 반환
        if(user.getRole() != ADMIN && !Objects.equals(user.getUserId(), payment.getOrder().getUser().getUserId())) {
            throw new AccessDeniedException("접근 권한이 없습니다.");
        }

        return PaymentDto.Response.of(payment);
    }

    @Transactional(readOnly = true)
    public Collection<PaymentDto.GetAllPaymentsResponse> getAllPayments(int page, int size, String sortBy, User user) {
        Sort.Direction direction = Sort.Direction.ASC;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Payment> paymentList;

        if(user.getRole().equals(ADMIN)) {
            // 관리자일 경우 모든 정보 조회
            paymentList = paymentRepository.findAllByIsDeletedFalse(pageable);
        }

        else{ // 유저일 경우 자신의 정보만 조회
            paymentList = paymentRepository.findAllNotDeletedForUser(user.getUserId(), pageable);
        }

        return paymentList.map(PaymentDto.GetAllPaymentsResponse::new).stream().toList();
    }

    @Transactional
    public PaymentDto.DeletePaymentResult deletePayment(UUID paymentId, User user) {

        if(!paymentRepository.existsById(paymentId)){
            throw new IllegalArgumentException("존재하지 않는 결제 정보입니다.");
        }

        paymentRepository.deleteById(paymentId, LocalDateTime.now(), user.getUserId());

        return PaymentDto.DeletePaymentResult.builder()
                .paymentId(paymentId)
                .build();
    }
}
