package com._NT.deliveryShop.controller;

import static com._NT.deliveryShop.domain.dto.DeliveryAddressDto.AddressResponse;
import static com._NT.deliveryShop.domain.dto.DeliveryAddressDto.CreateAddressRequest;
import static com._NT.deliveryShop.domain.dto.DeliveryAddressDto.ModifyAddressRequest;
import static com._NT.deliveryShop.domain.entity.UserRoleEnum.PreAuthorizeRole.ADMIN;
import static com._NT.deliveryShop.domain.entity.UserRoleEnum.PreAuthorizeRole.OWNER;
import static com._NT.deliveryShop.domain.entity.UserRoleEnum.PreAuthorizeRole.USER;

import com._NT.deliveryShop.domain.entity.UserRoleEnum;
import com._NT.deliveryShop.security.UserDetailsImpl;
import com._NT.deliveryShop.service.AddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "유저 배송지", description = "유저 배송지 등록, 조회, 수정, 삭제 API")
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserAddressController {

    private final AddressService addressService;
    private final SecurityFilterChain securityFilterChain;

    // 배송지 생성
    @PostMapping("/{userId}/addresses")
    @PreAuthorize("hasAnyRole(" + ADMIN + "," + OWNER + "," + USER + ")")
    @Operation(summary = "배송지 생성", description = "배송지를 생성합니다.")
    @ApiResponse(responseCode = "201", description = "배송지 생성 성공")
    @ResponseStatus(HttpStatus.CREATED)
    public AddressResponse createAddress(
            @Parameter(description = "유저 식별자", example = "1", required = true)
            @PathVariable Long userId,
            @RequestBody CreateAddressRequest requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        validateUserAccess(userId, userDetails);

        return addressService.createAddress(userDetails.getUser(), requestDto);
    }


    // 배송지 목록 조회
    @GetMapping("/{userId}/addresses")
    @PreAuthorize("hasAnyRole(" + ADMIN + "," + OWNER + "," + USER + ")")
    @Operation(summary = "배송지 목록 조회", description = "배송지 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "배송지 목록 조회 성공")
    public List<AddressResponse> getAddressList(
            @Parameter(description = "유저 식별자", example = "1", required = true)
            @PathVariable Long userId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        // 현재 인증된 사용자의 ID와 URL에 있는 ID가 일치하는지 확인
        validateUserAccess(userId, userDetails);

        return addressService.getAddressList(userDetails.getUser());
    }

    // 배송지 삭제
    @DeleteMapping("/{userId}/addresses/{addressId}")
    @PreAuthorize("hasAnyRole(" + ADMIN + "," + OWNER + "," + USER + ")")
    @Operation(summary = "배송지 삭제", description = "배송지를 삭제합니다.")
    @ApiResponse(responseCode = "200", description = "배송지 삭제 성공")
    public void deleteAddress(
            @Schema(description = "유저 식별자", example = "1", required = true)
            @PathVariable Long userId,
            @Schema(description = "배송지 식별자", example = "UUID", required = true)
            @PathVariable UUID addressId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        // 현재 인증된 사용자의 ID와 URL에 있는 ID가 일치하는지 확인
        validateUserAccess(userId, userDetails);

        addressService.deleteAddress(addressId, userDetails.getUser());
    }

    // 배송지 수정
    @PatchMapping("/{userId}/addresses/{addressId}")
    @PreAuthorize("hasAnyRole(" + ADMIN + "," + OWNER + "," + USER + ")")
    @Operation(summary = "배송지 수정", description = "배송지를 수정합니다.")
    @ApiResponse(responseCode = "200", description = "배송지 수정 성공")
    public AddressResponse modifyAddress(
            @Schema(description = "유저 식별자", example = "1", required = true)
            @PathVariable Long userId,
            @Schema(description = "배송지 식별자", example = "UUID", required = true)
            @PathVariable UUID addressId,
            @RequestBody ModifyAddressRequest requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        // 현재 인증된 사용자의 ID와 URL에 있는 ID가 일치하는지 확인
        validateUserAccess(userId, userDetails);

        return addressService.modifyAddress(addressId, requestDto, userDetails.getUser());
    }

    private void validateUserAccess(Long userId, UserDetailsImpl userDetails) {
        // 현재 인증된 사용자의 ID와 URL에 있는 ID가 일치하는지 확인, ADMIN은 예외
        if (!userDetails.getUser().getUserId().equals(userId) && !userDetails.getUser().getRole().equals(UserRoleEnum.ADMIN)) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }
    }
}
