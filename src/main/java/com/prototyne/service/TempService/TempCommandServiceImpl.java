package com.prototyne.service.TempService;

import com.prototyne.aws.s3.AmazonS3Manager;
import com.prototyne.web.dto.TempResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.prototyne.apiPayload.code.status.ErrorStatus;
import com.prototyne.apiPayload.exception.handler.TempHandler;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TempCommandServiceImpl implements TempCommandService {
    // S3 이미지 업로드 테스트
    private final AmazonS3Manager s3Manager;

    @Override
    public void CheckFlag(Integer flag) {
        if (flag == 1)
            throw new TempHandler(ErrorStatus.TEMP_EXCEPTION);
    }

    // 이미지 업로드 및 URL 리스트 반환
    public TempResponse.TempUploadDTO uploadImages(String directory, List<MultipartFile> images) {
        // S3에 이미지 업로드
        List<String> imageUrls = s3Manager.uploadFile(directory, images);

        // DTO 생성 (URL 리스트를 포함한 DTO)
        return TempResponse.TempUploadDTO.builder()
                .testStrings(imageUrls)
                .build();
    }
}