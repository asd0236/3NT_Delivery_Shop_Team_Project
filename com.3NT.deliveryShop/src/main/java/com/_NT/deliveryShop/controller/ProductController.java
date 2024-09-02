package com._NT.deliveryShop.controller;

import static com._NT.deliveryShop.common.codes.SuccessCode.DELETE_SUCCESS;
import static com._NT.deliveryShop.common.codes.SuccessCode.INSERT_SUCCESS;
import static com._NT.deliveryShop.common.codes.SuccessCode.SELECT_SUCCESS;
import static com._NT.deliveryShop.common.codes.SuccessCode.UPDATE_SUCCESS;
import static com._NT.deliveryShop.domain.dto.ProductDto.Create;
import static com._NT.deliveryShop.domain.dto.ProductDto.Patch;
import static com._NT.deliveryShop.domain.dto.ProductDto.Put;
import static com._NT.deliveryShop.domain.dto.ProductDto.Result;
import static com._NT.deliveryShop.domain.entity.UserRoleEnum.PreAuthorizeRole.ADMIN;
import static com._NT.deliveryShop.domain.entity.UserRoleEnum.PreAuthorizeRole.OWNER;

import com._NT.deliveryShop.common.response.ResultResponse;
import com._NT.deliveryShop.domain.dto.AmazonS3FileDto;
import com._NT.deliveryShop.repository.searchcondition.ProductSearchCondition;
import com._NT.deliveryShop.service.ProductImgService;
import com._NT.deliveryShop.service.ProductService;
import com._NT.deliveryShop.service.authorizer.ProductAuthorizer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@Tag(name = "상품", description = "상품 등록, 조회, 수정, 삭제 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService service;
    private final ProductImgService productImgService;
    private final ProductAuthorizer productAuthorizer;

    @PreAuthorize("hasAnyRole(" + ADMIN + "," + OWNER + ")")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "상품 등록", description = "상품을 등록합니다.")
    @ApiResponse(responseCode = "201", description = "상품 등록 성공")
    public ResultResponse<Result> postProduct(@RequestBody Create dto,
        Authentication authentication) {

        productAuthorizer.requireRestaurantOwner(authentication, dto.getRestaurantId());
        return ResultResponse.<Result>successBuilder()
            .result(service.createProduct(dto))
            .successCode(INSERT_SUCCESS)
            .build();
    }

    @PreAuthorize("hasAnyRole(" + ADMIN + "," + OWNER + ")")
    @PutMapping("/{productId}/image")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "상품 이미지 등록", description = "상품 이미지 등록합니다.")
    @ApiResponse(responseCode = "201", description = "상품 등록 성공")
    public ResultResponse<Result> putProductImg(@PathVariable UUID productId,
        @RequestParam(value = "file") MultipartFile multipartFile, Authentication authentication) {

        productAuthorizer.requireProductOwner(authentication, productId);

        AmazonS3FileDto.Put s3Dto = AmazonS3FileDto.Put.builder()
            .linkedUUID(productId)
            .multipartFile(multipartFile)
            .build();

        AmazonS3FileDto.Result productImgResult =
            productImgService.putProductImg(s3Dto);

        Patch patch = Patch.builder()
            .imageURL(productImgResult.getUploadFileUrl())
            .build();

        return ResultResponse.<Result>successBuilder()
            .result(service.patchProduct(productId, patch))
            .successCode(INSERT_SUCCESS)
            .build();
    }

    @GetMapping("/{productId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "상품 단건 조회", description = "상품을 단건 조회합니다.")
    @ApiResponse(responseCode = "200", description = "상품 조회 성공")
    public ResultResponse<Result> getProduct(@PathVariable UUID productId) {

        return ResultResponse.<Result>successBuilder()
            .result(service.readProduct(productId))
            .successCode(SELECT_SUCCESS)
            .build();
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "상품 전체 조회", description = "상품을 전체 조회합니다.")
    @ApiResponse(responseCode = "200", description = "상품 전체 조회 성공")
    public ResultResponse<List<Result>> getProducts(Pageable pageable) {

        return ResultResponse.<List<Result>>successBuilder()
            .result(service.readProducts(pageable))
            .successCode(SELECT_SUCCESS)
            .build();
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "상품 검색", description = "상품을 검색합니다.")
    @ApiResponse(responseCode = "200", description = "상품 검색 성공")
    public ResultResponse<List<Result>> searchProducts(
        @RequestParam(required = false) String nameLike, //해당 이름이 들어간 모든 상품
        Pageable pageable
    ) {
        ProductSearchCondition condition = ProductSearchCondition.builder()
            .nameLike(nameLike)
            .build();

        return ResultResponse.<List<Result>>successBuilder()
            .result(service.searchProducts(condition, pageable))
            .successCode(SELECT_SUCCESS)
            .build();
    }

    @PreAuthorize("hasAnyRole(" + ADMIN + "," + OWNER + ")")
    @PutMapping("/{productId}")
    @Operation(summary = "상품 수정", description = "상품을 수정합니다.")
    @ApiResponse(responseCode = "200", description = "상품 수정 성공")
    public ResultResponse<Result> putProduct(@PathVariable UUID productId,
        @RequestBody Put dto, Authentication authentication) {

        productAuthorizer.requireRestaurantOwner(authentication, dto.getRestaurantId());
        return ResultResponse.<Result>successBuilder()
            .result(service.putProduct(productId, dto))
            .successCode(UPDATE_SUCCESS)
            .build();
    }

    @PreAuthorize("hasAnyRole(" + ADMIN + "," + OWNER + ")")
    @PatchMapping("/{productId}/activation")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "상품 보임/숨김", description = "상품을 활성화/비활성화합니다.")
    @ApiResponse(responseCode = "200", description = "상품 활성화/비활성화 성공")
    public ResultResponse<Result> patchActivationProduct(
            @Schema(description = "상품 식별자", example = "UUID")
            @PathVariable UUID productId,
            @RequestBody Patch dto, Authentication authentication) {

        productAuthorizer.requireRestaurantOwner(authentication, dto.getRestaurantId());
        return ResultResponse.<Result>successBuilder()
            .result(service.patchProduct(productId, dto))
            .successCode(UPDATE_SUCCESS)
            .build();
    }

    @PreAuthorize("hasAnyRole(" + ADMIN + "," + OWNER + ")")
    @DeleteMapping("/{productId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "상품 삭제", description = "상품을 소프트 삭제합니다.")
    @ApiResponse(responseCode = "200", description = "상품 삭제 성공")
    public ResultResponse<Result.Deleted> softDeleteProduct(
            @Schema(description = "상품 식별자", example = "UUID")
            @PathVariable UUID productId,
            Authentication authentication) {

        productAuthorizer.requireProductOwner(authentication, productId);
        Result.Deleted productDeleteResult = service.softDeleteProduct(productId, authentication);
        productImgService.deleteProductImg(productId, authentication);
        return ResultResponse.<Result.Deleted>successBuilder()
            .result(productDeleteResult)
            .successCode(DELETE_SUCCESS)
            .build();
    }
}