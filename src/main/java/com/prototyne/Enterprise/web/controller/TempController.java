package com.prototyne.Enterprise.web.controller;

import com.prototyne.apiPayload.ApiResponse;
import com.prototyne.Users.converter.TempConverter;
import com.prototyne.Users.web.dto.TempResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/enterprise")
public class TempController {
    @Tag(name = "${swagger.tag.test}")
    @GetMapping("/api/tempTest")
    @Operation(summary = "health check API",
            description = "서버 상태 확인 API - 정상적으로 동작 중인지 확인")
    public ApiResponse<TempResponse.TempTestDTO> testAPI() {
        return ApiResponse.onSuccess(TempConverter.toTempTestDTO());
    }
}
