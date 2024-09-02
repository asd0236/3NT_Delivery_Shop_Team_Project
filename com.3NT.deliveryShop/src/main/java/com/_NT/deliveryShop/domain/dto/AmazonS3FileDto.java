package com._NT.deliveryShop.domain.dto;

import com._NT.deliveryShop.domain.entity.UploadFile;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;
import lombok.With;
import org.springframework.web.multipart.MultipartFile;

public interface AmazonS3FileDto {

    @With
    @Data
    @Builder
    class Put {

        private UUID linkedUUID;
        private MultipartFile multipartFile;

        public UploadFile asEntityWith(Result result) {
            return UploadFile.builder()
                .linkedUUID(linkedUUID)
                .originalFileName(result.getOriginalFileName())
                .uploadFilePath(result.getUploadFilePath())
                .uploadFileName(result.getUploadFileName())
                .uploadFileUrl(result.getUploadFileUrl())
                .build();
        }
    }


    @With
    @Data
    @Builder
    class Result {

        private String originalFileName;
        private String uploadFileName;
        private String uploadFilePath;
        private String uploadFileUrl;

        public static <T extends UploadFile> Result of(T uploadFile) {
            return Result.builder()
                .originalFileName(uploadFile.getOriginalFileName())
                .uploadFileName(uploadFile.getUploadFileName())
                .uploadFilePath(uploadFile.getUploadFilePath())
                .uploadFileUrl(uploadFile.getUploadFileUrl())
                .build();
        }

        @Data
        @Builder
        public static class Deleted {

            private String result;
            private String originalFileName;
            private String deletedUploadFileUrl;
        }
    }
}
