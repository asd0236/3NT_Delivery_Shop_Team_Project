package com._NT.deliveryShop.controller;

import static com._NT.deliveryShop.domain.dto.ProductDto.Create;
import static com._NT.deliveryShop.domain.dto.ProductDto.Patch;
import static com._NT.deliveryShop.domain.dto.ProductDto.Put;
import static com._NT.deliveryShop.domain.dto.ProductDto.Result;
import static com._NT.deliveryShop.domain.entity.UserRoleEnum.PreAuthorizeRole.ADMIN;
import static com._NT.deliveryShop.domain.entity.UserRoleEnum.PreAuthorizeRole.OWNER;

import com._NT.deliveryShop.domain.dto.AmazonS3FileDto;
import com._NT.deliveryShop.repository.searchcondition.ProductSearchCondition;
import com._NT.deliveryShop.service.ProductImgService;
import com._NT.deliveryShop.service.ProductService;
import com._NT.deliveryShop.service.authorizer.ProductAuthorizer;
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
    public Result postProduct(@RequestBody Create dto, Authentication authentication) {

        productAuthorizer.requireRestaurantOwner(authentication, dto.getRestaurantId());
        return service.createProduct(dto);
    }

    @PreAuthorize("hasAnyRole(" + ADMIN + "," + OWNER + ")")
    @PutMapping("/{productId}/image")
    @ResponseStatus(HttpStatus.CREATED)
    public Result putProductImg(@PathVariable UUID productId,
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

        return service.patchProduct(productId, patch);
    }

    @GetMapping("/{productId}")
    @ResponseStatus(HttpStatus.OK)
    public Result getProduct(@PathVariable UUID productId) {

        return service.readProduct(productId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Result> getProducts(Pageable pageable) {

        return service.readProducts(pageable);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public List<Result> searchProducts(
        @RequestParam(required = false) String nameLike, //해당 이름이 들어간 모든 상품
        Pageable pageable
    ) {
        ProductSearchCondition condition = ProductSearchCondition.builder()
            .nameLike(nameLike)
            .build();

        return service.searchProducts(condition, pageable);
    }

    @PreAuthorize("hasAnyRole(" + ADMIN + "," + OWNER + ")")
    @PutMapping("/{productId}")
    public Result putProduct(@PathVariable UUID productId,
        @RequestBody Put dto, Authentication authentication) {

        productAuthorizer.requireRestaurantOwner(authentication, dto.getRestaurantId());
        return service.putProduct(productId, dto);
    }

    @PreAuthorize("hasAnyRole(" + ADMIN + "," + OWNER + ")")
    @PatchMapping("/{productId}/activation")
    @ResponseStatus(HttpStatus.OK)
    public Result patchActivationProduct(@PathVariable UUID productId,
        @RequestBody Patch dto, Authentication authentication) {

        productAuthorizer.requireRestaurantOwner(authentication, dto.getRestaurantId());
        return service.patchProduct(productId, dto);
    }

    @PreAuthorize("hasAnyRole(" + ADMIN + "," + OWNER + ")")
    @DeleteMapping("/{productId}")
    @ResponseStatus(HttpStatus.OK)
    public Result.Deleted softDeleteProduct(@PathVariable UUID productId,
        Authentication authentication) {

        Result.Deleted productDeleteResult = service.softDeleteProduct(productId, authentication);
        productImgService.deleteProductImg(productId, authentication);
        return productDeleteResult;
    }
}