package com._NT.deliveryShop.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.With;

@With
@Entity
@Table(name = "p_restaurant_img")
@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class RestaurantImg extends UploadFile {

    // 음식점과 일대일 관계
    @OneToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @Builder(builderMethodName = "restaurantImgBuilder")
    public RestaurantImg(UploadFile uploadFile, Restaurant restaurant) {
        super(uploadFile);
        this.restaurant = restaurant;
    }

    public void setUploadFile(UploadFile uploadFile) {
        this.setOriginalFileName(uploadFile.getOriginalFileName());
        this.setUploadFilePath(uploadFile.getUploadFilePath());
        this.setUploadFileName(uploadFile.getUploadFileName());
        this.setUploadFileUrl(uploadFile.getUploadFileUrl());
    }
}
