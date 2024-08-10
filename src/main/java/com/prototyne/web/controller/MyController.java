package com.prototyne.web.controller;

import com.prototyne.apiPayload.ApiResponse;
import com.prototyne.apiPayload.code.status.ErrorStatus;
import com.prototyne.service.UserService.UserDetailServiceImpl;
import com.prototyne.web.dto.UserDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/my")
@Tag(name = "02. 내 정보 - 기본", description = "유저 정보 관련 API")
public class MyController {

    private final UserDetailServiceImpl userDetailService;

    @Tag(name = "${swagger.tag.auth}")
    @GetMapping("/detail")
    @Operation(summary = "유저 정보 조회 API - 인증 필요",
            description = "유저 정보 조회 API - 인증 필요",
            security = {@SecurityRequirement(name = "session-token")})
    public ApiResponse<UserDto.UserDetailResponse> getUserDetail(HttpServletRequest request) throws Exception {
        String accessToken = request.getHeader("Authorization").replace("Bearer ", "");
        log.info("JWT Token: {}", accessToken);
        UserDto.UserDetailResponse userDetailResponse = userDetailService.getUserDetail(accessToken);
        return ApiResponse.onSuccess(userDetailResponse);
    }

}
