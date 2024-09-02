package com._NT.deliveryShop.domain.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.UuidGenerator;

@MappedSuperclass
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Getter
@Setter
public class UploadFile extends Timestamped {

    @Id
    @UuidGenerator
    @Column(name = "upload_file_id")
    private UUID uploadFileId;

    private UUID likedUUID;

    private String originalFileName;

    private String uploadFilePath;

    private String uploadFileName;

    private String uploadFileUrl;

    @Builder
    public UploadFile(UUID likedUUID, String originalFileName, String uploadFilePath,
        String uploadFileName, String uploadFileUrl) {
        this.likedUUID = likedUUID;
        this.originalFileName = originalFileName;
        this.uploadFilePath = uploadFilePath;
        this.uploadFileName = uploadFileName;
        this.uploadFileUrl = uploadFileUrl;
    }

    @Builder
    public UploadFile(UploadFile base) {
        this.uploadFileId = base.uploadFileId;
        this.likedUUID = base.likedUUID;
        this.originalFileName = base.originalFileName;
        this.uploadFilePath = base.uploadFilePath;
        this.uploadFileName = base.uploadFileName;
        this.uploadFileUrl = base.uploadFileUrl;
    }
}
