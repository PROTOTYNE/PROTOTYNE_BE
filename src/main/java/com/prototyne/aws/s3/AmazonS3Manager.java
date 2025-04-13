package com.prototyne.aws.s3;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class AmazonS3Manager {

    private final S3Client s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    // S3에 여러 이미지 업로드
    public List<String> uploadFiles(String dirName, List<MultipartFile> files) {
        List<String> imageUrlList = new ArrayList<>();

        for (MultipartFile file : files) {
            try {
                String keyName = generateTestKeyName(dirName, file.getOriginalFilename());

                PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                        .bucket(bucket)
                        .key(keyName)
                        .contentType(file.getContentType())
                        .build();

                s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

                String imageUrl = String.format("https://%s.s3.%s.amazonaws.com/%s", bucket, region, keyName);
                imageUrlList.add(imageUrl);
            } catch (IOException e) {
                log.error("error at AmazonS3Manager uploadFile", e);
            }
        }
        return imageUrlList;
    }

    // S3의 여러 이미지 삭제
    public void deleteFiles(String dirName, List<String> fileUrls) {
        for (String url : fileUrls) {
            try {
                String keyName = findKeyName(url);
                s3Client.deleteObject(DeleteObjectRequest.builder()
                        .bucket(bucket)
                        .key(keyName)
                        .build());
            } catch (Exception e) {
                log.error("error at AmazonS3Manager deleteFile", e);
            }
        }
    }

    public String generateTestKeyName(String dirName, String originalName) {
        String ext = extractExt(originalName);
        String uuid = UUID.randomUUID().toString();
        return dirName + '/' + uuid + "." + ext;
    }

    private static String extractExt(String originalName) {
        int pos = originalName.lastIndexOf(".");
        return originalName.substring(pos + 1);
    }

    private static String findKeyName(String fileUrl) {
        // https://bucket.s3.ap-northeast-2.amazonaws.com/folder/uuid.jpg
        int index = fileUrl.indexOf(".amazonaws.com/") + ".amazonaws.com/".length();
        return fileUrl.substring(index);
    }
}
