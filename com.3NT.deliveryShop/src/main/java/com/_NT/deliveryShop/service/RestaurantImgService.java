package com._NT.deliveryShop.service;

import static com._NT.deliveryShop.domain.dto.AmazonS3FileDto.Put;
import static com._NT.deliveryShop.domain.dto.AmazonS3FileDto.Result;

import com._NT.deliveryShop.domain.entity.Restaurant;
import com._NT.deliveryShop.domain.entity.RestaurantImg;
import com._NT.deliveryShop.domain.entity.UploadFile;
import com._NT.deliveryShop.repository.RestaurantImgRepository;
import com._NT.deliveryShop.repository.helper.RepositoryHelper;
import com._NT.deliveryShop.service.authorizer.AuthenticationInspector;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RestaurantImgService {

    private final RestaurantImgRepository restaurantImgRepository;
    private final AmazonS3Service amazonS3Service;
    private final RepositoryHelper repositoryHelper;
    private final AuthenticationInspector authInspector;

    @Transactional
    public Result putRestaurantImg(Put dto) {

        Restaurant restaurant = repositoryHelper.findRestaurantOrThrow404(dto.getLinkedUUID());
        Result uploadResult = amazonS3Service.uploadFile(dto);
        UploadFile uploadFile = dto.asEntityWith(uploadResult);

        RestaurantImg restaurantImg;
        boolean existImg = restaurantImgRepository.existsByRestaurantRestaurantIdAndIsDeletedFalse(
            restaurant.getRestaurantId());

        if (existImg) {
            restaurantImg = restaurantImgRepository.findByRestaurantRestaurantIdAndIsDeletedFalse(
                restaurant.getRestaurantId()).get();
            amazonS3Service.deleteFile(restaurantImg);
            restaurantImg.setUploadFile(uploadFile);
        } else {
            restaurantImg = RestaurantImg.restaurantImgBuilder()
                .restaurant(restaurant)
                .uploadFile(uploadFile)
                .build();
        }

        return Result.of(restaurantImgRepository.save(restaurantImg));
    }

    @Transactional
    public void deleteRestaurantImg(UUID restaurantId, Authentication authentication) {
        Long deleterId = authInspector.getUserOrThrow(authentication).getUserId();
        RestaurantImg restaurantImg;
        boolean existImg = restaurantImgRepository.existsByRestaurantRestaurantIdAndIsDeletedFalse(
            restaurantId);
        if (existImg) {
            restaurantImg = restaurantImgRepository.findByRestaurantRestaurantIdAndIsDeletedFalse(
                    restaurantId)
                .get();
            Result.Deleted deletedResult = amazonS3Service.deleteFile(restaurantImg);
            restaurantImgRepository.softDeleteByUploadFileId(restaurantImg.getUploadFileId(),
                LocalDateTime.now(), deleterId);
        }
    }
}
