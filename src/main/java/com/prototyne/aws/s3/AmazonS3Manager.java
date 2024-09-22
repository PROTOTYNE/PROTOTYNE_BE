package com.prototyne.aws.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.prototyne.apiPayload.config.AmazonConfig;
import com.prototyne.apiPayload.exception.handler.TempHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class AmazonS3Manager {

    private final AmazonS3 amazonS3;
    private final AmazonConfig amazonConfig;

    // S3에 여러 이미지 업로드
    public List<String> uploadFiles(String dirName, List<MultipartFile> files) {

        // 반환 받을 이미지 url 리스트
        List<String> imageUrlList = new ArrayList<>();

        for (MultipartFile file : files) {
            // 메타데이터 설정
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType(file.getContentType());
            objectMetadata.setContentLength(file.getSize());

            // 이미지 파일에 대한 키네임 생성
            String originalName = file.getOriginalFilename();
            String keyName = generateTestKeyName(dirName, originalName);

            // S3에 업로드
            try {
                amazonS3.putObject(new PutObjectRequest(amazonConfig.getBucket(), keyName, file.getInputStream(), objectMetadata));
                // 업로드된 이미지 url 저장
                String imageUrl = amazonS3.getUrl(amazonConfig.getBucket(), keyName).toString();
                imageUrlList.add(imageUrl);
            } catch (IOException e) { // 업로드 실패
                log.error("error at AmazonS3Manager uploadFile : {}", (Object) e.getStackTrace());
            }
        }
        return imageUrlList;
    }

    // S3의 여러 이미지 삭제
    public void deleteFiles(String dirName, List<String> files) {
        for (String file : files) {
            try {
                String keyName = findKeyName(file);
                amazonS3.deleteObject(new DeleteObjectRequest(amazonConfig.getBucket(), keyName));
            } catch (Exception e) {
                log.error("error at AmazonS3Manager deletedFile : {}", (Object) e.getStackTrace());
            }
        }
    }

    /* 동일 이름의 이미지 방지를 위한, 이미지 이름 대신 랜덤 uuid 사용
       버킷/디렉터리/uuid.확장자 */

    // 디렉터리 + 키네임 생성
    public String generateTestKeyName(String dirName, String originalName){
        String ext = extractExt(originalName);
        String uuid = UUID.randomUUID().toString();
        return dirName + '/' + uuid + "." + ext;
    }

    // 파일 확장명 추출
    private static String extractExt(String originalName) {
        int pos = originalName.lastIndexOf(".");
        return originalName.substring(pos + 1);
    }

    private static String findKeyName(String fileName) {
        int slashIndex = fileName.lastIndexOf('/');
        int secondSlashIndex = fileName.lastIndexOf('/', slashIndex - 1);
        return fileName.substring(secondSlashIndex + 1);
    }
}
