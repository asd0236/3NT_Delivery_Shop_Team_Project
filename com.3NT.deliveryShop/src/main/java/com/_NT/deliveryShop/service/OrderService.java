package com._NT.deliveryShop.service;

import com._NT.deliveryShop.domain.dto.OrderCreateRequestDto;
import com._NT.deliveryShop.domain.dto.OrderCreateResponseDto;
import com._NT.deliveryShop.domain.entity.*;
import com._NT.deliveryShop.repository.DeliveryInfoRepository;
import com._NT.deliveryShop.repository.OrderRepository;
import com._NT.deliveryShop.repository.PaymentRepository;
import com._NT.deliveryShop.repository.RestaurantReposiotry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final RestaurantReposiotry restaurantRepository;
    private final DeliveryInfoRepository deliveryInfoRepository;

    @Transactional
    public OrderCreateResponseDto createOrder(OrderCreateRequestDto orderRequestDto, User user) {
        // 사용자 권한 가져와서 ADMIN, USER만 주문생성
        UserRoleEnum userRole = user.getRole();

        if (userRole != UserRoleEnum.ADMIN && userRole != UserRoleEnum.USER) {
            throw new IllegalArgumentException("사용자 권한이 없습니다.");
        }

        // 음식점 조회
        Restaurant restaurant = restaurantRepository.findById(orderRequestDto.getRestaurantId()).orElseThrow(
                () -> new NullPointerException("해당 음식점이 존재하지 않습니다."));

        // 배송지 정보 조회
        DeliveryInfo deliveryInfo = deliveryInfoRepository.findById(orderRequestDto.getDeliveryInfoId()).orElseThrow(
                () -> new NullPointerException("해당 배송지 정보가 존재하지 않습니다."));

        // 주문 생성(주문 생성 시 주문 상태는 PAYMENT_PENDING)
        Order order = new Order(user, restaurant, null, deliveryInfo, OrderStatus.PAYMENT_PENDING, orderRequestDto.getIsOnline());

        orderRepository.save(order);

        // 결제 로직 구현 시 주석 해제
//        try {
//             결제 처리 로직 수행(Payment Service 호출 반환 값은 Payment Entity)
//
//             결제 성공 시 주문에 결제 정보 추가
//             order.setPayment(payment);
//             order.setStatus(OrderStatus.PAYMENT_COMPLETED); // 결제 성공 시 주문 상태 변경
//
//             응답 dto 생성 및 반환
//            return new OrderCreateResponseDto(order);
//        } catch (Exception e) {
//             결제 실패 시 주문 취소 처리 로직 수행
//            order.setStatus(OrderStatus.PAYMENT_CANCELED); // 결제 실패 시 주문 상태 변경
//            return new OrderCreateResponseDto(order);
//        }

        return new OrderCreateResponseDto(order);
    }

}

