package com.prototyne.web.controller;

import com.prototyne.apiPayload.ApiResponse;
import com.prototyne.service.LoginService.KakaoServiceImpl;
import com.prototyne.web.dto.LoginDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/oauth2")
public class LoginController {
    private final KakaoServiceImpl kakaoService;

    @Tag(name="01. 회원가입")
    @GetMapping("/code/kakao")
    @Operation(summary = "Access Token 응답 API",
            description = "Access Token 응답",
            security = {@SecurityRequirement(name = "session-token")})
    public ApiResponse<LoginDto.KakaoTokenResponse> callback(@RequestParam("code") String code) {
        LoginDto.KakaoTokenResponse accessToken = kakaoService.getAccessToken(code);
        return ApiResponse.onSuccess(accessToken);
    }

    @Tag(name="01. 회원가입")
    @GetMapping("/user")
    @Operation(summary = "유저 정보 API - 인증 필요",
            description = "유저 정보 API - 인증 필요",
            security = {@SecurityRequirement(name = "session-token")})
    public ApiResponse<LoginDto.UserInfoResponse> userInfo(HttpServletRequest token) {
        String aouthtoken = token.getHeader("Authorization").replace("Bearer ", "");
        return ApiResponse.onSuccess(kakaoService.getUserInfo(aouthtoken));
    }
}