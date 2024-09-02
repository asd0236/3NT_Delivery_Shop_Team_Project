package com._NT.deliveryShop.controller;

import static com._NT.deliveryShop.domain.dto.RestaurantDto.Create;
import static com._NT.deliveryShop.domain.dto.RestaurantDto.Result;
import static com._NT.deliveryShop.domain.dto.RestaurantDto.Update;
import static com._NT.deliveryShop.domain.entity.UserRoleEnum.PreAuthorizeRole.ADMIN;
import static com._NT.deliveryShop.domain.entity.UserRoleEnum.PreAuthorizeRole.OWNER;

import com._NT.deliveryShop.domain.dto.AmazonS3FileDto;
import com._NT.deliveryShop.domain.dto.RestaurantDto;
import com._NT.deliveryShop.repository.searchcondition.RestaurantSearchCondition;
import com._NT.deliveryShop.service.ProductImgService;
import com._NT.deliveryShop.service.RestaurantImgService;
import com._NT.deliveryShop.service.RestaurantService;
import com._NT.deliveryShop.service.authorizer.RestaurantAuthorizer;
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
@RequestMapping("/api/v1/restaurants")
public class RestaurantController {

    private final RestaurantService service;
    private final RestaurantAuthorizer restaurantAuthorizer;
    private final RestaurantImgService restaurantImgService;
    private final ProductImgService productImgService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Result postRestaurant(@RequestBody Create dto,
        Authentication authentication) {
        return service.createRestaurant(dto, authentication);
    }

    @PreAuthorize("hasAnyRole(" + ADMIN + "," + OWNER + ")")
    @PutMapping("/{restaurantId}/image")
    @ResponseStatus(HttpStatus.CREATED)
    public Result putRestaurantImg(@PathVariable UUID restaurantId,
        @RequestParam(value = "file") MultipartFile multipartFile, Authentication authentication) {

        restaurantAuthorizer.requireRestaurantOwner(authentication, restaurantId);

        AmazonS3FileDto.Put s3Dto = AmazonS3FileDto.Put.builder()
            .linkedUUID(restaurantId)
            .multipartFile(multipartFile)
            .build();

        AmazonS3FileDto.Result restaurantImgResult =
            restaurantImgService.putRestaurantImg(s3Dto);

        Update update = Update.builder()
            .imageURL(restaurantImgResult.getUploadFileUrl())
            .build();

        return service.updateRestaurant(restaurantId, update, authentication);
    }

    @GetMapping("/{restaurantId}")
    public Result getRestaurant(@PathVariable UUID restaurantId) {
        return service.readRestaurant(restaurantId);
    }

    @GetMapping("/search")
    public List<Result> searchRestaurant(
        @RequestParam(required = false) String nameLike, //해당 이름이 들어간 모든 음식점
        @RequestParam(required = false) List<String> categoryNames, //해당 카테고리 이름들이 모두 들어간 모든 음식점
        Pageable pageable
    ) {
        RestaurantSearchCondition condition = RestaurantSearchCondition.builder()
            .nameLike(nameLike)
            .categoryNames(categoryNames)
            .build();

        return service.searchRestaurant(condition, pageable);
    }

    @GetMapping
    public List<Result> getRestaurants(
        Pageable pageable
    ) {

        return service.readRestaurants(pageable);
    }

    @PatchMapping("/{restaurantId}")
    public Result patchRestaurant(@PathVariable UUID restaurantId,
        @RequestBody Update dto, Authentication authentication) {
        return service.updateRestaurant(restaurantId, dto, authentication);
    }

    @DeleteMapping("/{restaurantId}")
    public Result.Deleted deleteRestaurant(@PathVariable UUID restaurantId,
        Authentication authentication) {

        RestaurantDto.Result.Deleted restaurantDeleteResult = service.deleteRestaurant(restaurantId,
            authentication);
        restaurantImgService.deleteRestaurantImg(restaurantId, authentication);
        productImgService.deleteProductImagesByRestaurantId(restaurantId, authentication);
        return restaurantDeleteResult;
    }
}