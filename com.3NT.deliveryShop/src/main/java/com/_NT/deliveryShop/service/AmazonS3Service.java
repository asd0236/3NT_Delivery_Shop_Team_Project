package com._NT.deliveryShop.service;

import static com._NT.deliveryShop.domain.dto.AmazonS3FileDto.Put;
import static com._NT.deliveryShop.domain.dto.AmazonS3FileDto.Result;

import com._NT.deliveryShop.domain.entity.UploadFile;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@Service
public class AmazonS3Service {

    private final AmazonS3 amazonS3Client;
    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    /**
     * S3로 파일 업로드
     */
    public List<Result> uploadFiles(UUID linkedUUID, List<MultipartFile> multipartFiles) {

        List<Result> results = new ArrayList<>();

        for (MultipartFile multipartFile : multipartFiles) {
            results.add(uploadFile(linkedUUID.toString(), multipartFile));
        }

        return results;
    }

    public Result uploadFile(UUID linkedUUID, MultipartFile file) {

        return uploadFile(linkedUUID.toString(), file);
    }

    public Result uploadFile(Put dto) {

        return uploadFile(dto.getLikedUUID(), dto.getMultipartFile());
    }

    private Result uploadFile(String uploadFilePath, MultipartFile multipartFile) {
        String originalFileName = multipartFile.getOriginalFilename();
        String uploadFileName = getUuidFileName(originalFileName);
        String uploadFileUrl = "";

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(multipartFile.getSize());
        objectMetadata.setContentType(multipartFile.getContentType());

        try (InputStream inputStream = multipartFile.getInputStream()) {

            String keyName = uploadFilePath + "/" + uploadFileName; // ex) 구분/년/월/일/파일.확장자

            // S3에 폴더 및 파일 업로드
            amazonS3Client.putObject(
                new PutObjectRequest(bucketName, keyName, inputStream, objectMetadata));

            // S3에 업로드한 폴더 및 파일 URL
            uploadFileUrl = amazonS3Client.getUrl(bucketName, keyName).toString();

        } catch (IOException e) {
            e.printStackTrace();
            log.error("Filed upload failed", e);
        }

        return
            Result.builder()
                .originalFileName(originalFileName)
                .uploadFilePath(uploadFilePath)
                .uploadFileName(uploadFileName)
                .uploadFileUrl(uploadFileUrl)
                .build();
    }

    /**
     * S3에 업로드된 파일 삭제
     */
    public Result.Deleted deleteFile(UUID likedUUID, String uploadFileName) {

        String result = "success";
        String uploadFileUrl = "";

        try {
            String keyName =
                likedUUID.toString() + "/" + uploadFileName; // ex) UUID/파일.확장자
            boolean isObjectExist = amazonS3Client.doesObjectExist(bucketName, keyName);
            uploadFileUrl = amazonS3Client.getUrl(bucketName, keyName).toString();
            if (isObjectExist) {
                amazonS3Client.deleteObject(bucketName, keyName);
            } else {
                result = "file not found";
            }
        } catch (Exception e) {
            log.debug("Delete File failed", e);
            result = "failed";
        }

        return Result.Deleted.builder()
            .result(result)
            .originalFileName(uploadFileName)
            .deletedUploadFileUrl(uploadFileUrl)
            .build();
    }

    public <T extends UploadFile> Result.Deleted deleteFile(T uploadFile) {
        return deleteFile(uploadFile.getLikedUUID(), uploadFile.getUploadFileName());
    }

    public List<Result.Deleted> deleteFiles(UUID likedUUID, List<String> names) {
        List<Result.Deleted> results = new ArrayList<>();

        for (String name : names) {
            results.add(deleteFile(likedUUID, name));
        }
        return results;
    }

    /**
     * UUID 파일명 반환
     */
    public String getUuidFileName(String fileName) {
        String ext = fileName.substring(fileName.indexOf(".") + 1);
        return UUID.randomUUID() + "." + ext;
    }

    public <T extends UploadFile> List<UUID> deleteFiles(List<T> uploadFiles) {
        List<UUID> likedUUIDs = new ArrayList<>();

        uploadFiles.forEach(uploadFile -> {
            deleteFile(uploadFile.getLinkedUUID(), uploadFile.getUploadFileName());
            likedUUIDs.add(uploadFile.getLinkedUUID());
        });

        return likedUUIDs;
    }
}