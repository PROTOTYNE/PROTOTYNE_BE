package com.prototyne.aws.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.prototyne.apiPayload.config.AmazonConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class AmazonS3Manager {

    private final AmazonS3 amazonS3;
    private final AmazonConfig amazonConfig;

    // S3에 이미지 업로드
    // 임시로 originalFilename 변수 사용 (uuid 엔티티 추후 구현?)
    public String uploadFile(MultipartFile file) throws IOException {

        // 메타데이터 설정
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        //metadata.setContentType(file.getContentType()); // Content-Type 설정

        // 키네임 설정
        String originalFilename = file.getOriginalFilename();
        String keyName = generateKeyName(originalFilename);

        PutObjectRequest request = new PutObjectRequest(amazonConfig.getBucket(),
                keyName, file.getInputStream(), metadata);

        amazonS3.putObject(request);

        return amazonS3.getUrl(amazonConfig.getBucket(), keyName).toString();
    }

    // 디렉터리 경로 생성
    private String generateKeyName(String originalFilename) {
        return amazonConfig.getTestPath() + '/' + originalFilename;
    }
}
