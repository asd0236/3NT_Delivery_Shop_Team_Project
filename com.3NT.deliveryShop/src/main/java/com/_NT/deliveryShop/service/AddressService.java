package com._NT.deliveryShop.service;

import com._NT.deliveryShop.domain.entity.DeliveryAddress;
import com._NT.deliveryShop.domain.entity.User;
import com._NT.deliveryShop.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    public AddressResponse createAddress(User user, CreateAddressRequest requestDto) {
        DeliveryAddress savedDeliveryAddress = addressRepository.save(new DeliveryAddress(user, requestDto.getAddress()));

        return new AddressResponse(user.getUserId(), savedDeliveryAddress.getDeliveryAddressId(), savedDeliveryAddress.getAddress());
    }

    /**
     * 사용자 주소 목록 조회
     * @param user: 사용자 정보
     * @return List<AddressResponse>: 주소 목록 응답 DTO userId, deliveryAdressId, address
     */
    @Transactional(readOnly = true)
    public List<AddressResponse> getAddressList(User user) {
        List<DeliveryAddress> savedListDeliveryAddress = addressRepository.findAllByUserId(user.getUserId());

        return savedListDeliveryAddress.stream()
                .map(deliveryAddress -> new AddressResponse(user.getUserId(), deliveryAddress.getDeliveryAddressId(), deliveryAddress.getAddress()))
                .toList();
    }

}
