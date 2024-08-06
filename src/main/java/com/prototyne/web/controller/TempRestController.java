package com.prototyne.web.controller;

import com.prototyne.web.dto.TempResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import com.prototyne.apiPayload.ApiResponse;
import com.prototyne.converter.TempConverter;
import com.prototyne.service.TempService.TempCommandService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/temp")
@RequiredArgsConstructor
public class TempRestController {

    private final TempCommandService tempQueryService;

    @PostMapping(value="/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<TempResponse.TempUploadDTO> imageUpload(@RequestPart("imageFiles")
                                                             List<MultipartFile> images) {
        TempResponse.TempUploadDTO tempUploadDTO =
                tempQueryService.uploadImages("test", images);
        return ApiResponse.onSuccess(tempUploadDTO);
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