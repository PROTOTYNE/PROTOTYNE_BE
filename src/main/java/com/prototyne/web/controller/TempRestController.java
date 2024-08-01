package com.prototyne.web.controller;

import com.prototyne.aws.s3.AmazonS3Manager;
import com.prototyne.web.dto.TempRequest;
import com.prototyne.web.dto.TempResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import com.prototyne.apiPayload.ApiResponse;
import com.prototyne.converter.TempConverter;
import com.prototyne.service.TempService.TempCommandService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/temp")
@RequiredArgsConstructor
public class TempRestController {

    private final TempCommandService tempQueryService;

    // S3 이미지 업로드 테스트
    private final AmazonS3Manager s3Manager;

    @PostMapping(value="/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<TempResponse.TempTestDTO> imageUpload(@RequestPart("imageFile")
                                                                 MultipartFile image) {
        //MultipartFile file = imageFile.getImageFile();
        try {
            // 파일 업로드
            String pictureUrl = s3Manager.uploadFile(image);

            // DTO 생성 (URL을 포함한 DTO)
            TempResponse.TempTestDTO responseDto = TempResponse.TempTestDTO.builder()
                    .testString(pictureUrl)
                    .build();

            // 성공 응답 반환
            return ApiResponse.onSuccess(responseDto);
        } catch (IOException e) {
            // 실패 응답 반환
            return ApiResponse.onFailure("500", "File upload failed: " + e.getMessage(), null);
        }
    }

    @GetMapping("/test")
    public ApiResponse<TempResponse.TempTestDTO> testAPI() {
        return ApiResponse.onSuccess(TempConverter.toTempTestDTO());
    }

    @GetMapping("/exception")
    public ApiResponse<TempResponse.TempExceptionDTO> exceptionAPI(@RequestParam Integer flag) {
        tempQueryService.CheckFlag(flag);
        return ApiResponse.onSuccess(TempConverter.toTempExceptionDTO(flag));
    }


}