package com._NT.deliveryShop.service;

import static com._NT.deliveryShop.domain.dto.RestaurantDto.Create;
import static com._NT.deliveryShop.domain.dto.RestaurantDto.Result;
import static com._NT.deliveryShop.domain.dto.RestaurantDto.Update;
import static com._NT.deliveryShop.domain.entity.UserRoleEnum.PreAuthorizeRole.ADMIN;
import static com._NT.deliveryShop.domain.entity.UserRoleEnum.PreAuthorizeRole.OWNER;

import com._NT.deliveryShop.domain.dto.CategoryDto;
import com._NT.deliveryShop.domain.entity.Category;
import com._NT.deliveryShop.domain.entity.Restaurant;
import com._NT.deliveryShop.domain.entity.User;
import com._NT.deliveryShop.repository.RestaurantRepository;
import com._NT.deliveryShop.repository.helper.RepositoryHelper;
import com._NT.deliveryShop.repository.implementaion.RestaurantRepositoryImpl;
import com._NT.deliveryShop.repository.searchcondition.RestaurantSearchCondition;
import com._NT.deliveryShop.service.authorizer.RestaurantAuthorizer;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final RepositoryHelper repoHelper;
    private final CategoryService categoryService;
    private final RestaurantAuthorizer restaurantAuthorizer;
    private final RestaurantRepositoryImpl restaurantRepositoryImpl;


    @PreAuthorize("hasAnyRole(" + ADMIN + "," + OWNER + ")")
    @Transactional
    public Result createRestaurant(Create dto, Authentication authentication) {
        User owner = repoHelper.findUserOrThrow404(dto.getOwnerId());
        restaurantAuthorizer.requireByOneself(authentication, owner);
        CategoryDto.Create categoryDto = CategoryDto.Create.builder()
            .name(dto.getCategoryName())
            .build();

        Category category = categoryService.createCategory(categoryDto);

        Restaurant restaurant = dto.asEntity(it -> it
            .withCategory(category));

        return Result.of(restaurantRepository.save(restaurant));
    }

    public Result readRestaurant(Long id) {
        return Result.of(repoHelper.findRestaurantOrThrow(id));
    }

    public List<Result> readRestaurants(Pageable pageable) {
        return Result.of(restaurantRepository.findAll(pageable));
    }

    public List<Result> searchRestaurant(RestaurantSearchCondition condition, Pageable pageable) {
        Page<Restaurant> restaurants = restaurantRepositoryImpl.search(condition, pageable);
        return Result.of(restaurants);
    }

    @PreAuthorize("hasAnyRole(" + ADMIN + OWNER + ")")
    @Transactional
    public Result updateRestaurant(Long id, Update dto, Authentication authentication) {
        Restaurant restaurant = repoHelper.findRestaurantOrThrow(id);
        restaurantAuthorizer.requireRestaurantOwner(authentication, restaurant);

        if (dto.getName() != null) {
            restaurant.setName(dto.getName());
        }
        if (dto.getCategoryName() != null) {
            restaurant.getCategory().setName(dto.getCategoryName());
        }
        if (dto.getMobileNumber() != null) {
            restaurant.setMobileNumber(dto.getMobileNumber());
        }
        if (dto.getAddress() != null) {
            restaurant.setAddress(dto.getAddress());
        }
        if (dto.getBusinessStartHours() != null) {
            restaurant.setBusinessStartHours(dto.getBusinessStartHours());
        }
        if (dto.getBusinessEndHours() != null) {
            restaurant.setBusinessEndHours(dto.getBusinessEndHours());
        }
        if (dto.getImageURL() != null) {
            restaurant.setImageURL(dto.getImageURL());
        }

        return Result.of(restaurantRepository.save(restaurant));
    }

    @PreAuthorize("hasRole(" + ADMIN + ")")
    @Transactional
    public Result.Deleted deleteRestaurant(Long id, Authentication authentication) {
        Restaurant restaurant = repoHelper.findRestaurantOrThrow(id);
        restaurantAuthorizer.requireRestaurantOwner(authentication, restaurant);

        restaurant.setIsDeleted(true);
        restaurant.setDeletedBy(id);

        return Result.Deleted.of(restaurantRepository.save(restaurant));
    }
}
