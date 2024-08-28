package com._NT.deliveryShop.service;

import static com._NT.deliveryShop.domain.dto.ProductDto.Create;
import static com._NT.deliveryShop.domain.dto.ProductDto.Put;
import static com._NT.deliveryShop.domain.dto.ProductDto.Result;

import com._NT.deliveryShop.domain.entity.Product;
import com._NT.deliveryShop.domain.entity.Restaurant;
import com._NT.deliveryShop.domain.entity.User;
import com._NT.deliveryShop.repository.ProductRepository;
import com._NT.deliveryShop.repository.helper.RepositoryHelper;
import com._NT.deliveryShop.repository.implementaion.ProductRepositoryImpl;
import com._NT.deliveryShop.repository.searchcondition.ProductSearchCondition;
import com._NT.deliveryShop.service.authorizer.AuthenticationInspector;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {


    private final ProductRepository productRepository;
    private final ProductRepositoryImpl productRepositoryImpl;
    private final RepositoryHelper repoHelper;
    private final AuthenticationInspector authInspector;

    @Transactional
    public Result createProduct(Create dto) {

        Restaurant restaurant = repoHelper.findRestaurantOrThrow404(dto.getRestaurantId());
        Product product = dto.asEntity(it -> it.withRestaurant(restaurant));

        return Result.of(productRepository.save(product));
    }

    public Result readProduct(UUID id) {

        return Result.of(repoHelper.findProductOrThrow404(id));
    }

    public List<Result> readProducts(Pageable pageable) {

        return Result.of(productRepository.findAllByIsDeletedFalse(pageable));
    }

    public List<Result> searchProducts(ProductSearchCondition condition, Pageable pageable) {

        return Result.of(productRepositoryImpl.search(condition, pageable));
    }

    @Transactional
    public Result putProduct(UUID productId, Put dto) {

        Product product = repoHelper.findProductOrThrow404(productId);
        Restaurant restaurant = repoHelper.findRestaurantOrThrow404(dto.getRestaurantId());

        product = dto.asEntity(it -> it
            .withRestaurant(restaurant)
            .withProductId(productId));

        return Result.of(productRepository.save(product));
    }

    @Transactional
    public Result.Deleted softDeleteProduct(UUID id, Authentication authentication) {

        Product product = repoHelper.findProductOrThrow404(id);
        User deleter = authInspector.getUserOrThrow(authentication);
        productRepository.softDeleteProduct(id, LocalDateTime.now(), deleter.getUserId());

        return Result.Deleted.of(product);
    }
}
