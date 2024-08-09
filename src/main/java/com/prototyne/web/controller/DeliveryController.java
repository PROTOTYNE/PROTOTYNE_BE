package com.prototyne.web.controller;

import com.prototyne.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/delivery")
public class DeliveryController {

    @Tag(name = "${swagger.tag.my-etc}")
    @GetMapping
    @Operation(summary = "배송지 조회 API - 인증 필요",
            description = "유저 배송지 조회",
            security = {@SecurityRequirement(name = "session-token")})
    public ApiResponse<String> callback(HttpServletRequest token) {
        String aouthtoken = token.getHeader("Authorization").replace("Bearer ", "");
        return ApiResponse.onSuccess("배송지 조회 성공");
    }

    @Tag(name = "${swagger.tag.my-etc}")
    @PatchMapping
    @Operation(summary = "배송지 수정 API - 인증 필요",
            description = "유저 배송지 수정",
            security = {@SecurityRequirement(name = "session-token")})
    public ApiResponse<String> add(HttpServletRequest token) {
        String aouthtoken = token.getHeader("Authorization").replace("Bearer ", "");
        return ApiResponse.onSuccess("배송지 수정 성공");
    }
}
