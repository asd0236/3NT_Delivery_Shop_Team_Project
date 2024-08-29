package com._NT.deliveryShop.service;

import com._NT.deliveryShop.domain.entity.*;
import com._NT.deliveryShop.repository.OrderRepository;
import com._NT.deliveryShop.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

import static com._NT.deliveryShop.domain.dto.OrderDto.*;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final RestaurantRepository restaurantRepository;

    @Transactional
    public CreateOrderResponse createOrder(CreateOrderRequest orderRequestDto, User user) {
        // 사용자 권한 가져와서 ADMIN, USER만 주문생성
        UserRoleEnum userRole = user.getRole();

        if (userRole != UserRoleEnum.ADMIN && userRole != UserRoleEnum.USER) {
            throw new IllegalArgumentException("사용자 권한이 없습니다.");
        }

        // 음식점 조회
        Restaurant restaurant = restaurantRepository.findById(orderRequestDto.getRestaurantId()).orElseThrow(
                () -> new NullPointerException("해당 음식점이 존재하지 않습니다."));

        // 배송지 정보

        // 상품 정보 조회

        // 주문 생성(주문 생성 시 주문 상태는 PAYMENT_PENDING)
        Order order = new Order(user, restaurant, null, OrderStatus.PAYMENT_PENDING, orderRequestDto.getIsOnline());

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

        return new CreateOrderResponse(order);
    }

    /**
     * 주문에 대한 단건 조회를 수행합니다.
     * @param orderId : 주문 ID를 전달 받습니다.
     * @param user : 사용자 정보를 전달 받습니다.
     * @return OrderResponseDto : 주문 정보를 반환합니다.
     */
    @Transactional(readOnly = true)
    public OrderResponse getOrder(UUID orderId, User user) {
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new NullPointerException("해당 주문이 존재하지 않습니다."));

        validateUserAccess(order, user);

        return new OrderResponse(order);
    }

    /**
     * 주문 삭제를 수행합니다. 주문 삭제 시 소프트 삭제로 처리합니다.
     * @param orderId : 주문 ID를 전달 받습니다.
     * @param user : 사용자 정보를 전달 받습니다.
     * @return OrderDeleteResponseDto : 삭제된 주문 ID를 반환합니다.
     */
    @Transactional
    public DeleteResponse deleteOrder(UUID orderId, User user) {
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new NullPointerException("해당 주문이 존재하지 않습니다."));

        validateUserAccess(order, user);
        orderRepository.deleteOrder(orderId, LocalDateTime.now(), user.getUserId());

        return new DeleteResponse(orderId);
    }


    /**
     * 주문 전체 조회를 수행합니다. 주문 조회 시 사용자 권한에 따라 다른 주문 정보를 조회합니다.
     * @param page : 페이지 번호를 전달 받습니다.
     * @param size : 페이지 사이즈를 전달 받습니다.
     * @param sortBy : 정렬 기준을 전달 받습니다.(ex. createdAt)
     * @param isAsc : 정렬 방향을 전달 받습니다.(true: 오름차순, false: 내림차순)
     * @param user : 사용자 정보를 전달 받습니다.
     * @return : 주문 정보를 반환합니다.
     */
    @Transactional(readOnly = true)
    public Page<OrderResponse> getAllOrders(int page, int size, String sortBy, boolean isAsc, User user) {
        // 정렬 방향 설정
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        // 정렬기준 sortBy, 정렬 방향isAsc로 정렬
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        // 사용자 권한 가져와서 ADMIN, OWNER, USER만 주문 조회
        UserRoleEnum userRole = user.getRole();

        Page<Order> orderList;

        if (userRole == UserRoleEnum.ADMIN) {
            // 사용자 권한이 ADMIN일 경우
            orderList = orderRepository.findAll(pageable);
        } else if (userRole == UserRoleEnum.OWNER) {
            // 사용자 권한이 OWNER일 경우
            orderList = orderRepository.findAllByRestaurantOwner(user, pageable);
        } else {
            // 사용자 권한이 USER일 경우
            orderList = orderRepository.findAllByUser(user, pageable);
        }

        return orderList.map(OrderResponse::new);
    }

    /**
     * 주문 수정을 수행합니다. 주문 수정 시 주문 정보(Status)를 수정합니다.
     * @param orderId : 주문 ID를 전달 받습니다.
     * @param orderModifyRequestDto : 주문 수정 정보를 전달 받습니다.
     * @param user : 사용자 정보를 전달 받습니다.
     * @return OrderResponseDto : 수정된 주문 정보를 반환합니다.
     */
    @Transactional
    public OrderResponse modifyOrder(UUID orderId, ModifyRequest orderModifyRequestDto, User user) {
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new NullPointerException("해당 주문이 존재하지 않습니다."));

        validateUserAccess(order, user);

        order.setStatus(orderModifyRequestDto.getStatus());

        return new OrderResponse(order);
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