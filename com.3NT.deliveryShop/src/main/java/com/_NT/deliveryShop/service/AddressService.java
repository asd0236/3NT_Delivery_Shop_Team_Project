package com._NT.deliveryShop.service;

import com._NT.deliveryShop.domain.entity.DeliveryAddress;
import com._NT.deliveryShop.domain.entity.User;
import com._NT.deliveryShop.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com._NT.deliveryShop.domain.dto.AddressDto.*;

@Service
@RequiredArgsConstructor
public class AddressService {
    private final AddressRepository addressRepository;

    /**
     * 사용자 주소 생성
     * @param user: 사용자 정보
     * @param requestDto: 주소 생성 요청 DTO address
     * @return CreateAddressResponse: 주소 생성 응답 DTO userId, deliveryAdressId, address
     */
    @Transactional
    public CreateAddressResponse createAddress(User user, CreateAddressRequest requestDto) {
        DeliveryAddress savedDeliveryAddress = addressRepository.save(new DeliveryAddress(user, requestDto.getAddress()));

        return new CreateAddressResponse(user.getUserId(), savedDeliveryAddress.getDeliveryAddressId(), savedDeliveryAddress.getAddress());
    }
}
