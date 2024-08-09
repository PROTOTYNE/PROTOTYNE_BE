package com.prototyne.web.controller;

import com.prototyne.apiPayload.ApiResponse;
import com.prototyne.converter.TempConverter;
import com.prototyne.service.LoginService.JwtManager;
import com.prototyne.service.TempService.TempCommandService;
import com.prototyne.web.dto.TempResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import com.prototyne.apiPayload.ApiResponse;
import com.prototyne.converter.TempConverter;
import com.prototyne.service.TempService.TempCommandService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TempRestController {

    private final TempCommandService tempQueryService;
    private final JwtManager JwtManager;

    @PostMapping(value="/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<TempResponse.TempUploadDTO> imageUpload(@RequestPart("imageFiles")
                                                             List<MultipartFile> images) {
        TempResponse.TempUploadDTO tempUploadDTO =
                tempQueryService.uploadImages("test", images);
        return ApiResponse.onSuccess(tempUploadDTO);
    }

    @Tag(name = "${swagger.tag.test}")
    @GetMapping("/health")
    @Operation(summary = "health check API",
            description = "서버 상태 확인 API - 정상적으로 동작 중인지 확인")
    public ApiResponse<TempResponse.TempTestDTO> testAPI() {
        return ApiResponse.onSuccess(TempConverter.toTempTestDTO());
    }

    @Tag(name = "${swagger.tag.test}")
    @GetMapping("/exception")
    @Operation(summary = "예외처리 테스트",
            description = "flag 값이 1일 경우 예외 발생")
    public ApiResponse<TempResponse.TempExceptionDTO> exceptionAPI(@RequestParam Integer flag) {
        tempQueryService.CheckFlag(flag);
        return ApiResponse.onSuccess(TempConverter.toTempExceptionDTO(flag));
    }

    @Tag(name = "${swagger.tag.test}")
    @GetMapping("/testaccount")
    @Operation(summary = "테스트 계정 임의 발급",
            description = """
                     개발 편의를 위한 테스트 계정 토큰 발급 api - **Swagger에서만 사용**
                                        \s
                     flag 값에 따라 토큰 발급 (1 ~ 5)
                    \s""")
    public ResponseEntity<String> jwks(@RequestParam Long flag) {
        if (flag > 5) {
            return ResponseEntity.badRequest().body("flag must be less than 5");
        } else if (flag < 1) {
            return ResponseEntity.badRequest().body("flag must be greater than 1");
        }
        String jwt = JwtManager.createJwt(flag);
        return ResponseEntity.ok(jwt);
    }
}