package com._NT.deliveryShop.controller;

import static com._NT.deliveryShop.domain.dto.RestaurantDto.Create;
import static com._NT.deliveryShop.domain.dto.RestaurantDto.Result;
import static com._NT.deliveryShop.domain.dto.RestaurantDto.Update;

import com._NT.deliveryShop.repository.searchcondition.RestaurantSearchCondition;
import com._NT.deliveryShop.service.RestaurantService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/restaurants")
public class RestaurantController {

    private final RestaurantService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Result postRestaurant(@RequestBody Create dto,
        Authentication authentication) {
        return service.createRestaurant(dto, authentication);
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
        return service.deleteRestaurant(restaurantId, authentication);
    }
}