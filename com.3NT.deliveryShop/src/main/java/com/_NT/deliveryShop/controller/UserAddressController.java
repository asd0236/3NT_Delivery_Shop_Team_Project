package com._NT.deliveryShop.controller;

import com._NT.deliveryShop.common.codes.ErrorCode;
import com._NT.deliveryShop.common.codes.SuccessCode;
import com._NT.deliveryShop.common.exception.CustomException;
import com._NT.deliveryShop.common.response.ResultResponse;
import com._NT.deliveryShop.domain.entity.UserRoleEnum;
import com._NT.deliveryShop.security.UserDetailsImpl;
import com._NT.deliveryShop.service.AddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com._NT.deliveryShop.domain.dto.DeliveryAddressDto.*;
import static com._NT.deliveryShop.domain.entity.UserRoleEnum.PreAuthorizeRole.*;

@Tag(name = "유저 배송지", description = "유저 배송지 등록, 조회, 수정, 삭제 API")
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserAddressController {

    private final AddressService addressService;

    // 배송지 생성
    @PostMapping("/{userId}/addresses")
    @PreAuthorize("hasAnyRole(" + ADMIN + "," + OWNER + "," + USER + ")")
    @Operation(summary = "배송지 생성", description = "배송지를 생성합니다.")
    @ApiResponse(responseCode = "201", description = "배송지 생성 성공")
    public ResultResponse<AddressResponse> createAddress(
            @Parameter(description = "유저 식별자", example = "1", required = true)
            @PathVariable Long userId,
            @RequestBody @Valid CreateAddressRequest requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        validateUserAccess(userId, userDetails);

        AddressResponse address = addressService.createAddress(userDetails.getUser(), requestDto);
        return ResultResponse.<AddressResponse>builder()
                .result(address)
                .resultCode(SuccessCode.INSERT_SUCCESS.getStatus())
                .resultMessage(SuccessCode.INSERT_SUCCESS.getMessage())
                .build();
    }

    // 배송지 목록 조회
    @GetMapping("/{userId}/addresses")
    @PreAuthorize("hasAnyRole(" + ADMIN + "," + OWNER + "," + USER + ")")
    @Operation(summary = "배송지 목록 조회", description = "배송지 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "배송지 목록 조회 성공")
    public ResultResponse<List<AddressResponse>> getAddressList(
            @Parameter(description = "유저 식별자", example = "1", required = true)
            @PathVariable Long userId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        // 현재 인증된 사용자의 ID와 URL에 있는 ID가 일치하는지 확인
        validateUserAccess(userId, userDetails);

        List<AddressResponse> addressList = addressService.getAddressList(userDetails.getUser());

        return ResultResponse.<List<AddressResponse>>builder()
                .result(addressList)
                .resultCode(SuccessCode.SELECT_SUCCESS.getStatus())
                .resultMessage(SuccessCode.SELECT_SUCCESS.getMessage())
                .build();
    }

    // 배송지 삭제
    @DeleteMapping("/{userId}/addresses/{addressId}")
    @PreAuthorize("hasAnyRole(" + ADMIN + "," + OWNER + "," + USER + ")")
    @Operation(summary = "배송지 삭제", description = "배송지를 삭제합니다.")
    @ApiResponse(responseCode = "200", description = "배송지 삭제 성공")
    public ResultResponse<UUID> deleteAddress(
            @Schema(description = "유저 식별자", example = "1", required = true)
            @PathVariable Long userId,
            @Schema(description = "배송지 식별자", example = "UUID", required = true)
            @PathVariable UUID addressId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        // 현재 인증된 사용자의 ID와 URL에 있는 ID가 일치하는지 확인
        validateUserAccess(userId, userDetails);

        addressService.deleteAddress(addressId, userDetails.getUser());

        return ResultResponse.<UUID>builder()
                .result(addressId)
                .resultCode(SuccessCode.DELETE_SUCCESS.getStatus())
                .resultMessage(SuccessCode.DELETE_SUCCESS.getMessage())
                .build();
    }

    // 배송지 수정
    @PatchMapping("/{userId}/addresses/{addressId}")
    @PreAuthorize("hasAnyRole(" + ADMIN + "," + OWNER + "," + USER + ")")
    @Operation(summary = "배송지 수정", description = "배송지를 수정합니다.")
    @ApiResponse(responseCode = "200", description = "배송지 수정 성공")
    public ResultResponse<AddressResponse> modifyAddress(
            @Schema(description = "유저 식별자", example = "1", required = true)
            @PathVariable Long userId,
            @Schema(description = "배송지 식별자", example = "UUID", required = true)
            @PathVariable UUID addressId,
            @RequestBody @Valid ModifyAddressRequest requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        // 현재 인증된 사용자의 ID와 URL에 있는 ID가 일치하는지 확인
        validateUserAccess(userId, userDetails);

        AddressResponse addressResponse = addressService.modifyAddress(addressId, requestDto, userDetails.getUser());
        return ResultResponse.<AddressResponse>builder()
                .result(addressResponse)
                .resultCode(SuccessCode.UPDATE_SUCCESS.getStatus())
                .resultMessage(SuccessCode.UPDATE_SUCCESS.getMessage())
                .build();
    }

    private void validateUserAccess(Long userId, UserDetailsImpl userDetails) {
        // 현재 인증된 사용자의 ID와 URL에 있는 ID가 일치하는지 확인, ADMIN은 예외
        if (!userDetails.getUser().getUserId().equals(userId) && !userDetails.getUser().getRole().equals(UserRoleEnum.ADMIN)) {
            throw new CustomException(ErrorCode.FORBIDDEN_ERROR, "해당 사용자의 권한이 없습니다.");
        }
    }
}
