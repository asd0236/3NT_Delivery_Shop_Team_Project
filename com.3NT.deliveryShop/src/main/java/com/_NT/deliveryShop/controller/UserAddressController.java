package com._NT.deliveryShop.controller;

import com._NT.deliveryShop.domain.entity.UserRoleEnum;
import com._NT.deliveryShop.security.UserDetailsImpl;
import com._NT.deliveryShop.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com._NT.deliveryShop.domain.dto.AddressDto.*;
import static com._NT.deliveryShop.domain.entity.UserRoleEnum.PreAuthorizeRole.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserAddressController {

    private final AddressService addressService;

    // 배송지 생성
    @PostMapping("/{userId}/addresses")
    @PreAuthorize("hasAnyRole(" + ADMIN + "," + OWNER + "," + USER + ")")
    public CreateAddressResponse createAddress(
            @PathVariable Long userId,
            @RequestBody CreateAddressRequest requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        validateUserAccess(userId, userDetails);

        return addressService.createAddress(userDetails.getUser(), requestDto);
    }


    private void validateUserAccess(Long userId, UserDetailsImpl userDetails) {
        // 현재 인증된 사용자의 ID와 URL에 있는 ID가 일치하는지 확인
        if (!userDetails.getUser().getUserId().equals(userId) && !userDetails.getUser().getRole().equals(UserRoleEnum.ADMIN)) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }
    }
}
