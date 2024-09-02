package com._NT.deliveryShop.service;

import static com._NT.deliveryShop.domain.dto.AmazonS3FileDto.Put;
import static com._NT.deliveryShop.domain.dto.AmazonS3FileDto.Result;

import com._NT.deliveryShop.domain.entity.Product;
import com._NT.deliveryShop.domain.entity.ProductImg;
import com._NT.deliveryShop.domain.entity.UploadFile;
import com._NT.deliveryShop.repository.ProductImgRepository;
import com._NT.deliveryShop.repository.helper.RepositoryHelper;
import com._NT.deliveryShop.service.authorizer.AuthenticationInspector;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductImgService {

    private final ProductImgRepository productImgRepository;
    private final AmazonS3Service amazonS3Service;
    private final RepositoryHelper repositoryHelper;
    private final AuthenticationInspector authInspector;

    @Transactional
    public Result putProductImg(Put dto) {

        Product product = repositoryHelper.findProductOrThrow404(dto.getLinkedUUID());
        Result uploadResult = amazonS3Service.uploadFile(dto);
        UploadFile uploadFile = dto.asEntityWith(uploadResult);

        ProductImg productImg;
        boolean existImg = productImgRepository.existsByProductProductIdAndIsDeletedFalse(
            product.getProductId());

        if (existImg) {
            productImg = productImgRepository.findByProductProductIdAndIsDeletedFalse(
                product.getProductId()).get();
            amazonS3Service.deleteFile(productImg);
            productImg.setUploadFile(uploadFile);
        } else {
            productImg = ProductImg.productImgBuilder()
                .product(product)
                .uploadFile(uploadFile)
                .build();
        }

        return Result.of(productImgRepository.save(productImg));
    }

    @Transactional
    public void deleteProductImg(UUID productId, Authentication authentication) {
        Long deleterId = authInspector.getUserOrThrow(authentication).getUserId();
        ProductImg productImg;
        boolean existImg = productImgRepository.existsByProductProductIdAndIsDeletedFalse(
            productId);
        if (existImg) {
            productImg = productImgRepository.findByProductProductIdAndIsDeletedFalse(productId)
                .get();
            Result.Deleted deletedResult = amazonS3Service.deleteFile(productImg);
            productImgRepository.softDeleteByUploadFileId(productImg.getUploadFileId(),
                LocalDateTime.now(), deleterId);
        }
    }

    @Transactional
    public void deleteProductImagesByRestaurantId(UUID restaurantId,
        Authentication authentication) {
        Long deleterId = authInspector.getUserOrThrow(authentication).getUserId();
        List<ProductImg> productImages = productImgRepository
            .findAllInBatchByProductRestaurantRestaurantIdAndIsDeletedFalse(restaurantId);

        if (!productImages.isEmpty()) {

            List<UUID> UUIDs = amazonS3Service.deleteFiles(productImages);

            productImgRepository.
                softDeleteByUploadFileIds(UUIDs, LocalDateTime.now(), deleterId);
        }
    }
}
