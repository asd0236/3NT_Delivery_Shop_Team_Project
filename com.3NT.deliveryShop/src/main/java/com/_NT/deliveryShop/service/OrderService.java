package com._NT.deliveryShop.service;

import com._NT.deliveryShop.domain.dto.OrderCreateRequestDto;
import com._NT.deliveryShop.domain.dto.OrderCreateResponseDto;
import com._NT.deliveryShop.domain.dto.OrderDeleteResponseDto;
import com._NT.deliveryShop.domain.dto.OrderResponseDto;
import com._NT.deliveryShop.domain.entity.*;
import com._NT.deliveryShop.repository.DeliveryInfoRepository;
import com._NT.deliveryShop.repository.OrderRepository;
import com._NT.deliveryShop.repository.PaymentRepository;
import com._NT.deliveryShop.repository.RestaurantReposiotry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final RestaurantReposiotry restaurantRepository;
    private final PaymentRepository paymentRepository;
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

    /**
     * 주문에 대한 단건 조회를 수행합니다.
     * @param orderId : 주문 ID를 전달 받습니다.
     * @param user : 사용자 정보를 전달 받습니다.
     * @return OrderResponseDto : 주문 정보를 반환합니다.
     */
    @Transactional(readOnly = true)
    public OrderResponseDto getOrder(UUID orderId, User user) {
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new NullPointerException("해당 주문이 존재하지 않습니다."));

        validateUserAccess(order, user);

        return new OrderResponseDto(order);
    }

    /**
     * 주문 삭제를 수행합니다. 주문 삭제 시 소프트 삭제로 처리합니다.
     * @param orderId : 주문 ID를 전달 받습니다.
     * @param user : 사용자 정보를 전달 받습니다.
     * @return OrderDeleteResponseDto : 삭제된 주문 ID를 반환합니다.
     */
    public OrderDeleteResponseDto deleteOrder(UUID orderId, User user) {
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new NullPointerException("해당 주문이 존재하지 않습니다."));

        validateUserAccess(order, user);
        orderRepository.deleteOrder(orderId, LocalDateTime.now(), user.getUserId());

        return new OrderDeleteResponseDto(orderId);
    }

    // 접근 권한 확인
    private void validateUserAccess(Order order, User user) {
        // 해당하는 가게 주인(OWNER)과 주문한 사용자(USER),관리자(ADMIN) 주문 조회 가능
        UserRoleEnum userRole = user.getRole();

        switch (userRole) {
            case ADMIN:
                // ADMIN일 경우 모든 주문 접근 가능
                break;
            case USER:
                if (!order.getUser().getUserId().equals(user.getUserId())) {
                    throw new IllegalArgumentException("해당 주문에 접근할 수 없습니다. 본인의 주문만 조회할 수 있습니다.");
                }
                break;
            case OWNER:
                if (!order.getRestaurant().getOwner().getUserId().equals(user.getUserId())) {
                    throw new IllegalArgumentException("해당 주문에 접근할 수 없습니다. 본인의 가게 주문만 조회할 수 있습니다.");
                }
                break;
            default:
                throw new IllegalArgumentException("사용자 권한이 없습니다.");
        }
    }

}

